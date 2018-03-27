CREATE TABLE BuryPoint(
  buryPointId TEXT,
  areano TEXT,
  areanoname TEXT,
  buttonno TEXT,
  buttonname TEXT
);
INSERT INTO BuryPoint(buryPointId,areano,areanoname,buttonno,buttonname) 
SELECT 'SubMenuActivity_tv_titlemenu_tofavor','morepoint','气泡框','morepoint-mainpage','首页' UNION ALL 
SELECT 'SubMenuActivity_tv_titlemenu_exit','morepoint','气泡框','morepoint-exit','退出' UNION ALL 
SELECT 'SubMenuActivity_tv_titlemenu_qrcode','morepoint','气泡框','morepoint-qrcode','扫一扫' UNION ALL 
SELECT 'SubMenuActivity_tv_titlemenu_service','morepoint','气泡框','morepoint-onlineservice','在线客服' UNION ALL 
SELECT 'SubMenuActivity_navbar_more_btn','maintitle','固定标题区','maintitle-morepoint','...' UNION ALL 
SELECT 'MainActivity_exit_btn','maintitle','固定标题区','maintitle-saveexit','安全退出' UNION ALL 
SELECT 'MainActivity_navbar_qrcode_btn','maintitle','固定标题区','maintitle-qrcode','扫一扫' UNION ALL 
SELECT 'SubMenuActivity_return_btn','maintitle','固定标题区','maintitle-mainpage','主页' UNION ALL 
SELECT 'MainActivity_index_btn','maintitle','固定标题区','maintitle-mainpage','主页' UNION ALL 
SELECT 'QRCodeScanActivity_return_btn','maintitle','固定标题区','maintitle-return','返回' UNION ALL 
SELECT 'MainActivity_cover_user_photo','favorite-login','最爱登录区','favorite-login','点击登录' UNION ALL 
SELECT 'MainActivity_remember_loginid_checkbox','login','登录','login-remember','记住用户名' UNION ALL 
SELECT 'MainActivity_forget_password_link','login','登录','login-forget','忘记密码' UNION ALL 
SELECT 'MainActivity_reg_btn','login','登录','login-selfreg','自助注册' UNION ALL 
SELECT 'EditUserInfoActivity_rl_edituser_info','favorite-userimage','头像区','favorite-userimage','头像' UNION ALL 
SELECT 'EditUserInfoActivity_upload_album_btn','favorite-userimage','头像区','favorite-userimage-gallery','相册获取' UNION ALL 
SELECT 'EditUserInfoActivity_upload_camera_btn','favorite-userimage','头像区','favorite-userimage-camera','拍照获取' UNION ALL 
SELECT 'MoreMenuActivity_index_btn','favorite-add','添加','favorite-add-mainpage','主页' UNION ALL 
SELECT 'MoreMenuActivity_alluse_name','favorite-add','添加','favorite-add-mostly','大家都在用标签' UNION ALL 
SELECT 'MoreMenuActivity_whole_name','favorite-add','添加','favorite-add-all','全部标签' UNION ALL 
SELECT 'MainActivity_navbar_mylife_cal_btn','smartservice-cal','金融日历','smartservice-cal-cal','日历图标' UNION ALL 
SELECT 'MainActivity_navbar_mylife_add_btn','smartservice-cal','金融日历','smartservice-cal-add','添加图标' UNION ALL 
SELECT 'MainActivity_ll_smartlife_delete','smartservice-cal','金融日历','smartservice-cal-del','提醒列表删除按钮' UNION ALL 
SELECT 'MainActivity_myassert','mine-account','账户总览','mine-account-allproperty','我的资产' UNION ALL 
SELECT 'MainActivity_mydet','mine-account','账户总览','mine-account-allindebited','我的负债' ;
