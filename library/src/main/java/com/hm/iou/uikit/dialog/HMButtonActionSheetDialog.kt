package com.hm.iou.uikit.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.uikit.R
import kotlinx.android.synthetic.main.uikit_dialog_button_actionsheet_list.*

/**
 * Created by hjy on 2019/8/21.
 *
 * 底部弹出的 ActionSheet 弹出，但是其样式是 Button
 */
class HMButtonActionSheetDialog(val builder: Builder) : Dialog(builder.context, R.style.UikitAlertDialogStyle_FromBottom) {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uikit_dialog_button_actionsheet_list)
        initWindow()
        initContent()
    }

    private fun initWindow() {
        val win = window ?: return
        val attrs = win.attributes
        attrs.width = WindowManager.LayoutParams.MATCH_PARENT
        attrs.height = WindowManager.LayoutParams.WRAP_CONTENT
        win.setGravity(Gravity.BOTTOM)
        win.attributes = attrs
    }

    private fun initContent() {
        if (builder.mTitle.isNullOrEmpty()) {
            tv_dialog_title.visibility = View.GONE
            view_dialog_divider.visibility = View.GONE
        } else {
            tv_dialog_title.visibility = View.VISIBLE
            view_dialog_divider.visibility = View.VISIBLE
            tv_dialog_title.text = builder.mTitle
        }

        rv_dialog_list.layoutManager = LinearLayoutManager(context)
        val adapter = SheetListAdapter()
        rv_dialog_list.adapter = adapter
        adapter.setNewData(builder.mDataList)
        adapter.setOnItemChildClickListener { _, _, position ->
            this.dismiss()
            builder.mListener?.let {
                it.onItemClick(position)
            }
        }
    }


    internal inner class SheetListAdapter : BaseQuickAdapter<ActionItem, BaseViewHolder>(R.layout.uikit_item_button_action_sheet) {

        override fun convert(helper: BaseViewHolder?, item: ActionItem?) {
            helper ?: return
            item ?: return

            helper.setText(R.id.btn_item_action, item.item)
            val btn: Button = helper.getView(R.id.btn_item_action)
            when (item.btnStyle) {
                ButtonStyle.Yellow -> {
                    btn.setBackgroundResource(R.drawable.uikit_selector_btn_main)
                    btn.setTextColor(context.resources.getColor(R.color.uikit_text_main_content))
                }
                ButtonStyle.BlackBordered -> {
                    btn.setBackgroundResource(R.drawable.uikit_selector_btn_bordered)
                    btn.setTextColor(context.resources.getColor(R.color.uikit_text_sub_content))
                }
            }
            helper.addOnClickListener(R.id.btn_item_action)
        }
    }

    enum class ButtonStyle(val style: Int) {
        Yellow(0),          //黄色按钮
        BlackBordered(1)    //白底、黑色边框
    }

    class ActionItem(val item: String, val btnStyle: ButtonStyle)

    class Builder(val context: Context) {

        var mCancelable = true
        var mCanceledOnTouchOutside = true
        var mTitle: CharSequence? = null
        var mDataList: MutableList<ActionItem>? = null
        var mListener: OnItemClickListener? = null

        fun setCancelable(cancelable: Boolean): Builder {
            mCancelable = cancelable
            return this
        }

        fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): Builder {
            mCanceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        fun setTitle(title: CharSequence?): Builder {
            mTitle = title
            return this
        }

        fun addAction(vararg action: Pair<String, ButtonStyle>): Builder {
            mDataList = mutableListOf()
            for (item in action) {
                mDataList?.add(ActionItem(item.first, item.second))
            }
            return this
        }

        fun setOnItemClickListener(listener: OnItemClickListener): Builder {
            mListener = listener
            return this
        }

        fun create(): HMButtonActionSheetDialog {
            val dialog = HMButtonActionSheetDialog(this)
            dialog.setCancelable(mCancelable)
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
            return dialog
        }
    }

}
