package cin.hello.com.mvpmodule;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cin.hello.com.mvpmodule.base.BaseMvpActivity;
import cin.hello.com.mvpmodule.bean.BaseObjectBean;
import cin.hello.com.mvpmodule.contract.MainContract;
import cin.hello.com.mvpmodule.presenter.MainPresenter;
import cin.hello.com.mvpmodule.util.ProgressDialog;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View {
    //@BindView(R2.id.et_username_login)
    TextInputEditText etUsernameLogin;
    //@BindView(R2.id.et_password_login)
    TextInputEditText etPasswordLogin;

    Button btn_signin_login;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mvp_main;
    }

    @Override
    public void initView() {
        etUsernameLogin = findViewById(R.id.et_username_login);
        etPasswordLogin = findViewById(R.id.et_password_login);
        btn_signin_login = findViewById(R.id.btn_signin_login);
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        btn_signin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getUsername().isEmpty() || getPassword().isEmpty()) {
                    Toast.makeText(MainActivity.this, "帐号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.login(getUsername(), getPassword());
            }
        });
    }

    /**
     * @return 帐号
     */
    private String getUsername() {
        return etUsernameLogin.getText().toString().trim();
    }

    /**
     * @return 密码
     */
    private String getPassword() {
        return etPasswordLogin.getText().toString().trim();
    }

    @Override
    public void onSuccess(BaseObjectBean bean) {
        Toast.makeText(this, bean.getErrorMsg(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        ProgressDialog.getInstance().show(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialog.getInstance().dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        //ButterKnife.bind(this);
    }
}
