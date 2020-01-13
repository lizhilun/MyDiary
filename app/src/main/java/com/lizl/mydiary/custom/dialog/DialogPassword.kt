package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.widget.EditText
import com.jungly.gridpasswordview.GridPasswordView
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.custom.function.setOnPasswordChangedListener
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.dialog_password_confirm.*

class DialogPassword(context: Context, private val passwordOperation: Int, private val password: String? = null,
                     private val onInputFinishListener: (String) -> Unit) : BaseDialog(context)
{

    private var firstPassword: String? = null

    private lateinit var curOperationState: OperationState

    companion object
    {
        const val PASSWORD_OPERATION_CHECK = 1
        const val PASSWORD_OPERATION_NEW = 2
        const val PASSWORD_OPERATION_MODIFY = 3
        const val PASSWORD_OPERATION_INPUT = 4
    }

    override fun getDialogContentViewResId() = R.layout.dialog_password_confirm

    override fun initView()
    {
        gpv_password.setOnPasswordChangedListener { onPasswordInputFinish(it) }

        when (passwordOperation)
        {
            PASSWORD_OPERATION_CHECK  -> turnToOperationState(OperationState.CheckState)
            PASSWORD_OPERATION_NEW    -> turnToOperationState(OperationState.InputNewState)
            PASSWORD_OPERATION_MODIFY -> turnToOperationState(OperationState.CheckOldState)
            PASSWORD_OPERATION_INPUT  -> turnToOperationState(OperationState.InputPasswordState)
        }

        val editText = gpv_password.getEditText() ?: return
        gpv_password.post { UiUtil.showInputKeyboard(editText) }
    }

    private fun GridPasswordView.getEditText(): EditText?
    {
        val viewCount = this.childCount
        for (i in 0 until viewCount)
        {
            val view = gpv_password.getChildAt(i)
            if (view is EditText)
            {
                return view
            }
        }
        return null
    }

    private fun turnToOperationState(operationState: OperationState)
    {
        curOperationState = operationState
        tv_notify.text = operationState.getStateNotify()
        clearInputPassword(false)
    }

    private fun turnToNextOperationState(inputPassword: String)
    {
        val nextState = curOperationState.nextState()
        if (nextState == null)
        {
            onInputFinishListener.invoke(inputPassword)
            dismiss()
        }
        else
        {
            turnToOperationState(nextState)
        }
    }

    private fun turnToWrongOperationState()
    {
        clearInputPassword(true)
        val wrongState = curOperationState.wrongState() ?: return
        firstPassword = null
        turnToOperationState(wrongState)
    }

    private fun onPasswordInputFinish(inputPassword: String)
    {
        if (curOperationState.needSaveInputPassword())
        {
            firstPassword = inputPassword
        }
        if (checkInputPassword(inputPassword))
        {
            turnToNextOperationState(inputPassword)
        }
        else
        {
            turnToWrongOperationState()
        }
    }

    private fun checkInputPassword(inputPassword: String): Boolean
    {
        return if (firstPassword.isNullOrBlank())
        {
            password.isNullOrBlank() || password == inputPassword
        }
        else
        {
            firstPassword == inputPassword
        }
    }

    private fun clearInputPassword(needDelay: Boolean)
    {
        gpv_password.postDelayed({ gpv_password.clearPassword() }, if (needDelay) 100L else 0)
    }

    override fun getDialogWidth() = (UiUtil.getScreenWidth() * 0.9).toInt()

    enum class OperationState
    {
        CheckState
        {
            override fun getStateNotify() = UiApplication.instance.getString(R.string.please_input_password_to_confirm)
        },
        CheckOldState
        {
            override fun nextState() = InputNewState

            override fun getStateNotify() = UiApplication.instance.getString(R.string.please_input_old_password)
        },
        InputNewState
        {
            override fun nextState() = InputAgainState

            override fun getStateNotify() = UiApplication.instance.getString(R.string.please_input_new_password)

            override fun needSaveInputPassword() = true
        },
        ReInputNewState
        {
            override fun nextState() = InputAgainState

            override fun getStateNotify() = UiApplication.instance.getString(R.string.not_same_input_so_please_input_new_password_again)

            override fun needSaveInputPassword() = true
        },
        InputAgainState
        {
            override fun getStateNotify() = UiApplication.instance.getString(R.string.please_input_new_password_again)

            override fun wrongState() = ReInputNewState
        },
        InputPasswordState
        {
            override fun getStateNotify() = UiApplication.instance.getString(R.string.please_input_password_to_confirm)
        };

        open fun nextState(): OperationState? = null

        open fun wrongState(): OperationState? = null

        open fun getStateNotify() = ""

        open fun needSaveInputPassword() = false
    }
}