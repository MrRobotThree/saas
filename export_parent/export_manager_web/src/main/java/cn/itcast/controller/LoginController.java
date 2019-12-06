package cn.itcast.controller;


import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import cn.itcast.utils.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController{

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;
    @Autowired
    private ModuleService moduleService;

	@RequestMapping("/login")
	public String login(String email, String password) {
//        0、email和password不能为空
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)){
            request.setAttribute("error","邮箱或密码不能为空");
//            重定向：相当于让浏览器重新发起了一次请求，之前的request和response丢失了
            return "forward:/login.jsp";  //请求转发
        }
//        使用shiro框架的认证（登录）方式 有三步 1、创建令牌 （用户输入的信息用户名和密文的密码） 2、获取主题  3、开始认证
        password = new Md5Hash(password,email,2).toString();
//        1、创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken(email,password);
//        2、获取主题
        Subject subject = SecurityUtils.getSubject();
//        3、开始认证
        try {
            subject.login(token); //AuthenticationToken
        } catch (AuthenticationException e) {
            request.setAttribute("error","邮箱或密码错误");
            return "forward:/login.jsp";  //请求转发
        }
//        页面上需要从session中获取当前登录人和当前登录人的菜单

//        获取主角：user
        User user = (User) subject.getPrincipal();

        session.setAttribute("loginUser",user);
        //        根据用户查询菜单
        List<Module> moduleList = moduleService.findModuleListByUser(user);
        session.setAttribute("modules",moduleList);
        return "home/main";
/*
//	    1、根据email查询用户
       User user = userService.findByEmail(email);
//        2、判断是否能根据email查询到用户数据
        if(user==null){
            request.setAttribute("error","邮箱错误");
            return "forward:/login.jsp";  //请求转发
        }else{
            //        3、如果能查询到 ，把页面上传过来的password加密在跟user中的password比较
            String password_db = user.getPassword(); //密文
//            把页面上的密码加密
            String password_page = new Md5Hash(password,email,2).toString();
            if(!password_db.equals(password_page)){
                request.setAttribute("error","密码错误");
                return "forward:/login.jsp";  //请求转发
            }
        }
//        email和password都正确
//        把当前登录人的信息存到session中
        session.setAttribute("loginUser",user);

//        根据用户查询菜单
       List<Module> moduleList = moduleService.findModuleListByUser(user);
       session.setAttribute("modules",moduleList);
*/


	}

    //微信登陆
    @RequestMapping("/weixinlogin")
    public String weixinlogin(String code,String state) {
        // 判断
        if (code==null) {
            System.out.println("用户未授权...");
        }
        //获取到了code值，回调没有问题
        //1.根据code获取access_token和openId
        String atUtl = "https://api.weixin.qq.com/sns/oauth2/access_token"+"?code="+code+"&appid=wx3bdb1192c22883f3&secret=db9d6b88821df403e5ff11742e799105&grant_type=authorization_code";
        Map<String, Object> map1 = HttpUtils.sendGet(atUtl);
        Object access_token = map1.get("access_token");
        Object openid = map1.get("openid").toString();
        if(access_token == null && openid == null) {
            System.out.println("用户未登录...");
        }
        //2.根据access_token和openId获取微信用户信息
        /*可通过获取用户基本信息中的unionid来区分用户的唯一性，
        因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，
        用户的unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。
        * */
        String wxurl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token +"&openid="+openid;
        Map<String, Object> map2 = HttpUtils.sendGet(wxurl);
        Object unionid = map2.get("unionid");
        String unionid_s = unionid.toString();
        User user1 = userService.finbByUnionid(unionid_s);
        if (user1==null) {
            //微信绑定页面
            request.setAttribute("unionid",unionid_s);
            return "forward:email.jsp";
        }
        //创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken(user1.getEmail(), user1.getPassword());
        //获取主题
        Subject subject = SecurityUtils.getSubject();
        //开始认证
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            //e.printStackTrace();
            request.setAttribute("error","邮箱或者密码错误");
            return "forward:login.jsp";
        }
        //获取主角
        User user = (User) subject.getPrincipal();//从shiro中获取当前登录人
        //把登录人放入到session
        session.setAttribute("loginUser",user);
        List<Module> moduleList = moduleService.findModuleListByUser(user);
        session.setAttribute("modules",moduleList);
        return "home/main";
    }


    //微信登陆之邮箱绑定
    @RequestMapping("/emailAdd")
    public String emailAdd(String email,String password,String unionid) {
        //0.email和password不能为空
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            request.setAttribute("unionid",unionid);
            request.setAttribute("error","邮箱或密码不能为空");
            //重定向：相当于让浏览器重新发起了一次请求，之前的request和response丢失了
            return "forward:/email.jsp"; //请求转发
        }
        //使用shiro的认证方式：1.创建令牌 2.获取主题 3.开始认证
        password = new Md5Hash(password, email, 2).toString(); //把页面上输入的明文的密码转成密文的
        //创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        //获取主题
        Subject subject = SecurityUtils.getSubject();
        //开始认证
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            //e.printStackTrace();
            request.setAttribute("error","邮箱或者密码错误");
            request.setAttribute("unionid",unionid);
            return "forward:email.jsp";
        }
        User user_email = userService.findByEmail(email);
        User user_wx = new User();
        user_wx.setId(user_email.getId());
        user_wx.setWxunionid(unionid);
        userService.update(user_wx);
        //获取主角
        User user = (User) subject.getPrincipal();//从shiro中获取当前登录人
        //把登录人放入到session
        session.setAttribute("loginUser",user);
        List<Module> moduleList = moduleService.findModuleListByUser(user);
        session.setAttribute("modules",moduleList);
        return "home/main";
    }



    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        SecurityUtils.getSubject().logout();   //登出
//        session.removeAttribute("loginUser");  //不用
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
