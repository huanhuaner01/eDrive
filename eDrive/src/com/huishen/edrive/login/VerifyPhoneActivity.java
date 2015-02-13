package com.huishen.edrive.login;


import com.huishen.edrive.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 验证手机号
 * @author zhanghuan
 *
 */
public class VerifyPhoneActivity extends Activity implements
		OnClickListener {

	private EditText editPhoneNumber, editVerifyCode;
	private Button btnVerify, btnStart ;
	private TextView tvProtocal;
	private TextView title ; //标题
	private ImageButton back ;
	

	public static final Intent getIntent(Context context) {
		Intent intent = new Intent(context, VerifyPhoneActivity.class);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_phone);
		initWidgets();
	}

	private void initWidgets() {
		editPhoneNumber = (EditText) findViewById(R.id.verify_edit_number);
		editVerifyCode = (EditText) findViewById(R.id.verify_edit_vcode);
		btnVerify = (Button) findViewById(R.id.verify_btn_verify);
		btnStart = (Button) findViewById(R.id.verify_btn_start);
		tvProtocal = (TextView) findViewById(R.id.verify_tv_protocal);
		back = (ImageButton) findViewById(R.id.header_back);
		title = (TextView)findViewById(R.id.header_title);
		title.setText(this.getResources().getString(R.string.str_verify_phone_title));
		tvProtocal.setText(buildProtocalText());
		btnVerify.setOnClickListener(this);
		back.setOnClickListener(this) ;
		// 用于限制验证码的长度
		editVerifyCode.addTextChangedListener(new TextWatcher() {

			final int validLength = getResources().getInteger(
					R.integer.verifycode_valid_length);

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				btnStart.setEnabled((s.length() == validLength) ? true : false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.verify_btn_verify:
			sendVerifyRequest();
			break;
		case R.id.verify_btn_start:
			sendVerifyCode();
			break;
		case R.id.header_back:
			this.finish();
			break ;
		default:
			break;
		}
	}

	/**
	 * 发送验证码再次提交给服务器。
	 */
	private final void sendVerifyCode() {
		// TODO add net request here
	}

	/**
	 * 完成手机号码的发送和计时等工作。
	 */
	private final void sendVerifyRequest() {
		// check phone
		String num = editPhoneNumber.getText().toString();
		if (!num.matches("(86|\\+86)1\\d{10}")) {
			Toast.makeText(
					VerifyPhoneActivity.this,
					getResources().getString(
							R.string.str_verify_phone_err_not_valid_number),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// TODO add net request here
		final int validtime = getResources().getInteger(
				R.integer.verifycode_valid_time_seconds);
		// maybe should be a class field to support cancel
		new CountDownTimer(validtime * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				btnVerify.setText((int) (millisUntilFinished / 1000));
			}

			@Override
			public void onFinish() {
				btnVerify.setText(R.string.str_verify_phone_err_request_retry);
			}
		}.start();
	}

	private final CharSequence buildProtocalText() {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(getResources().getString(
				R.string.str_verify_phone_protocal_prefix));
		int protocalStartIndex = ssb.length();
		ssb.append(getResources().getString(
				R.string.str_verify_phone_protocal_name));
		ClickableSpan clickableSpan = new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				Log.d("", "clicked.");
				displayProtocal();
			}
		};
		tvProtocal.setMovementMethod(LinkMovementMethod.getInstance());
		ssb.setSpan(clickableSpan, protocalStartIndex, ssb.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.main_color));
		ssb.setSpan(colorSpan, protocalStartIndex, ssb.length(),
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		return ssb;
	}

	private final void displayProtocal() {
		Intent i = new Intent(this ,ServiceInfoActivity.class);
		this.startActivity(i) ;
	}

}
