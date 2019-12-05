package com.lizl.mydiary.custom.dialog

import android.content.Context
import android.text.TextUtils
import android.widget.EditText
import com.jungly.gridpasswordview.GridPasswordView
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.dialog_password_confirm.*

class DialogPassword(context: Context, private val passwordOperation: Int, private val password: String?, private val onInputFinishListener: (String) -> Unit) :
        BaseDialog(context)
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

    constructor(context: Context, onInputFinishListener: (String) -> Unit) : this(context, PASSWORD_OPERATION_NEW, null, onInputFinishListener)

    constructor(context: Context, password: String, isCheckOnly: Boolean, onInputFinishListener: (String) -> Unit) : this(context,
            if (isCheckOnly) PASSWORD_OPERATION_CHECK else PASSWORD_OPERATION_MODIFY, password, onInputFinishListener)

    override fun getDialogContentViewResId() = R.layout.dialog_password_confirm

    override fun initView()
    {
        gpv_password.setOnPasswordChangedListener(object : GridPasswordView.OnPasswordChangedListener
        {
            override fun onInputFinish(psw: String)
            {
                onPasswordInputFinish(psw)
            }

            override fun onTextChanged(psw: String)
            {

            }
        })

        when (passwordOperation)
        {
            PASSWORD_OPERATION_CHECK  -> turnToOperationState(OperationState.CheckState)
            PASSWORD_OPERATION_NEW    -> turnToOperationState(OperationState.InputNewState)
            PASSWORD_OPERATION_MODIFY -> turnToOperationState(OperationState.CheckOldState)
            PASSWORD_OPERATION_INPUT  -> turnToOperationState(OperationState.InputPasswordState)
        }
        val viewCount = gpv_password.childCount
        for (i in 0 until viewCount)
        {
            val view = gpv_password.getChildAt(i)
            if (view is EditText)
            {
                view.post { UiUtil.showInputKeyboard(view) }
                break
            }
        }
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
        val wrongState = curOperationState.wrongState()
        if (wrongState != null)
        {
            firstPassword = null
            turnToOperationState(wrongState)
        }
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
        return if (TextUtils.isEmpty(firstPassword))
        {
            TextUtils.isEmpty(password) || password == inputPassword
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