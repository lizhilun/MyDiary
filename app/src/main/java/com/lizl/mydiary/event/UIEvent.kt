package com.lizl.mydiary.event

class UIEvent(val event: String, val value: Any?)
{
    constructor(event: String) : this(event, null)
}