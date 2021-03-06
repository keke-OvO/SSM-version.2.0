package com.kk.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kk.pojo.Admin;
import com.kk.pojo.Books;
import com.kk.pojo.LendHistory;
import com.kk.pojo.Reader;
import com.kk.service.AdminService;
import com.kk.service.BookService;
import com.kk.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

@Controller
@RequestMapping("/book")
public class MyController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ReaderService readerService;

    // 1. 跳转到登录页面
    @RequestMapping("/tologin")
    public String login(){
        return "login";
    }

    // 2. 跳转到登注册页面
    @RequestMapping("/toregist")
    public String regist(){
        return "regist";
    }

    // 3. 登录验证
    @RequestMapping("/dologin")
    @ResponseBody
    public Map list(String name,
                    String password,
                    String access,
                    HttpSession session){
        HashMap<String, Object> verification = new HashMap<>();
        try{
            //1.管理员
            if(access.equals("0")){
                Admin ad = new Admin();
                ad.setName(name);
                ad.setPwd(password);
                Admin admin = adminService.select(ad);

                if(admin != null){
                    session.setAttribute("admin",admin);
                    verification.put("stu",0);
                }else{
                    verification.put("stu",2);
                    verification.put("msg","用户名或密码错误");
                }
            }

            //2.读者
            if(access.equals("1")){
                Reader rd = new Reader();
                rd.setName(name);
                rd.setPwd(password);
                Reader reader = readerService.select(rd);
                if(reader != null){
                    session.setAttribute("reader",reader);
                    verification.put("stu",1);
                }else{
                    verification.put("stu",2);
                    verification.put("msg","用户名或密码错误");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            verification.put("stu",3);
            verification.put("msg","服务器异常,请改天登录");
        }

        return verification;
    }



    @RequestMapping("/toupdatepage")
    public String toUpdatePage(int id,Model model){
        Books book = bookService.queryById(id);
        model.addAttribute("book",book);
        return "updateBook";
    }

//   4.主页面，书厅
    @RequestMapping("/homepage")
    public String ListBooks(Model model){
        return "homePage";
    }

    //5.展示图书表格，以layui固定的表格格式返回
    @RequestMapping("/booklist")
    @ResponseBody
    public Map<String, Object> allBook(@RequestParam(defaultValue = "1", value = "page")
                                 Integer pageNum,
                                       @RequestParam(defaultValue = "5", value = "limit")
                                 Integer pageSize,
                                 String bookname, String author) {

        HashMap<Object, Object> map = new HashMap<>();
        if (bookname!=null)  map.put("bookname",bookname);
        if (author!=null)  map.put("author",author);

        PageHelper.startPage(pageNum,pageSize);
        List<Books> booksList = bookService.queryAllBook(map);
        PageInfo<Books> pageInfo = new PageInfo<>(booksList);

        // Layui table 组件要求返回的格式
        HashMap<String, Object> tableObj = new HashMap<>();
        tableObj.put("code", 0);
        tableObj.put("msg", "");
        tableObj.put("count",pageInfo.getTotal());
        tableObj.put("data",booksList);

        return tableObj;
    }


//    6.通过id查找一本书籍
    @RequestMapping("/findbyid")
    public String findById(int id, Model model){
        Books book = bookService.queryById(id);
        model.addAttribute("book", book);
        return "lendHistory";
    }

    //   7. 跳转到新增书籍页面
    @RequestMapping("/toadd")
    public String toAddPage(){
        return "bookAdd";
    }

//   8. 添加一本书籍
    @RequestMapping("/addbook")
    @ResponseBody
    public Map addBook(Books book){
        HashMap<String, Object> hashMap = new HashMap<>();
        try{
            bookService.addBook(book);
            hashMap.put("msg","添加成功！");
            hashMap.put("code",0);
        }catch (Exception e){
            e.printStackTrace();
            hashMap.put("msg","添加失败！");
            hashMap.put("code",1);
        }
        return hashMap;
    }

//   9. 删除一本书籍
    @RequestMapping("/delebook")
    @ResponseBody
    public Map deleteBook(int bookID){
        HashMap<String, Object> hashmap = new HashMap<>();
        try{
            bookService.deleteBook(bookID);
            hashmap.put("msg","删除成功！");
            hashmap.put("code",0);
        }catch (Exception e){
             e.printStackTrace();
             hashmap.put("msg","删除失败！");
            hashmap.put("code",1);
        }
        return hashmap;
    }

//   10. 跳转到修改页面
    @RequestMapping("/toupdate")
    public String toupdate(int bookID,Model model){
        Books book = bookService.queryById(bookID);
        model.addAttribute("book",book);
        return "updateBook";
    }

    //修改书籍
    @RequestMapping("/updatebook")
    @ResponseBody
    public Map updateBook(Books books){
        HashMap<Object, Object> hashmap = new HashMap<>();
        try{
            bookService.updateBook(books);
            hashmap.put("msg","修改成功！");
            hashmap.put("code",0);
        }catch (Exception e){
            e.printStackTrace();
            hashmap.put("msg","修改失败！");
            hashmap.put("code",1);
        }
        return hashmap;
    }

    //11.退出
    @RequestMapping("/loginout")
    public String loginout(HttpSession session){
        //调用session.invalidate(),代表该session被删除，所有信息丢失
        session.invalidate();
        return "login";
    }

    //12.注册存入
    @RequestMapping("/regist")
    @ResponseBody
    public Map regist(Reader reader){
    System.out.println(reader);
        readerService.add(reader);
        HashMap<String, Object> map = new HashMap<>();
        map.put("msg","注册成功！即将跳往登录页面...");
        return map;
    }

    //跳转到修改密码界面
    @RequestMapping("/toAlter")
    public String toAlterpwd(Integer num, Model model) {
        model.addAttribute("num",num);
        return "alterPwd";
    }


    //检测密码是否正确
    @RequestMapping("/check")
    @ResponseBody
    public Map<Object, Object> ckeckPwd(String num,String opwd,String adminId,String id) {
        HashMap<Object, Object> map = new HashMap<>();
        if (num.equals("0")) {
            map.put("adminId",adminId);
            Admin admin1 = adminService.check(map);
            if (admin1.getPwd().equals(opwd)){
                map.put("code",0);
                map.put("msg","密码正确");
            }else {
                map.put("code",1);
                map.put("msg","密码错误");
            }
        }
        else if (num.equals("1")) {
            map.put("id",id);
            Reader reader1 = readerService.check(map);
            if (reader1.getPwd().equals(opwd)){
                map.put("code",0);
                map.put("msg","密码正确");
            }else {
                map.put("code",1);
                map.put("msg","密码错误");
            }
        }
        return map;
    }

    //修改密码
    @RequestMapping("/alterPwd")
    @ResponseBody
    public Map<Object, Object> alterPwd(String num, Admin admin, Reader reader) {
        HashMap<Object, Object> map = new HashMap<>();
        if (num.equals("0")) {
            adminService.altpwd(admin);
            map.put("code",0);
            map.put("msg","修改成功");
        }
        else if (num.equals("1")) {
            readerService.altpwd(reader);
            map.put("code",0);
            map.put("msg","修改成功");
        }else map.put("msg","修改失败");
        return map;
    }

    //跳转到读者列表
    @RequestMapping("/toReader")
    public String toReader(){
        return "readerList";
    }

    @RequestMapping("/toHomepageRe")
    public String toHomepageReader(){
        return "homepageReader";
    }

    //读者列表
    @RequestMapping("/readerList")
    @ResponseBody
    public Map<String, Object> allReader(@RequestParam(defaultValue = "1", value = "page")
                                               Integer pageNum,
                                       @RequestParam(defaultValue = "5", value = "limit")
                                               Integer pageSize,
                                       Reader reader) {
        HashMap<Object, Object> map = new HashMap<>();

        PageHelper.startPage(pageNum,pageSize);
        List list = readerService.allReader();
        PageInfo<Reader> pageInfo = new PageInfo<>(list);

        // Layui table 组件要求返回的格式
        HashMap<String, Object> tableObj = new HashMap<>();
        tableObj.put("code", 0);
        tableObj.put("msg", "");
        tableObj.put("count",pageInfo.getTotal());
        tableObj.put("data",list);

        return tableObj;
    }


    //删除读者信息
    @RequestMapping("/readerDel")
    @ResponseBody
    public Map del(int id){
        HashMap<Object, Object> map = new HashMap<>();
        readerService.del(id);
        map.put("msg","删除成功");
        return map;
    }

    //跳到借阅历史记录
    @RequestMapping("/toLendHistory")
    public String toLend(){
        return "lendHistory";
    }

    //借阅
    @RequestMapping("/lend")
    @ResponseBody
    public Map lendBook(Books book,HttpSession session){
        HashMap<Object, Object> map = new HashMap<>();
        Books findBook = bookService.queryById(book.getBookID());
        Books lendBook = bookService.queryLenById(book.getBookID());
        if (findBook.getBookCounts()==0){
            map.put("status",0);
            return map;
        }else if (findBook.getBookCounts()>0){
            if (lendBook==null){
                bookService.addLendBook(findBook);
                Reader reader = (Reader) session.getAttribute("reader");
                LendHistory lendHistory = new LendHistory();

                lendHistory.setName(reader.getName());
                lendHistory.setBookID(findBook.getBookID());
                lendHistory.setBookName(findBook.getBookName());
                lendHistory.setLendTime(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                bookService.addLendHis(lendHistory);
                bookService.minusOne();
                map.put("status",1);

            }
            else if (lendBook!=null && lendBook.getBookName().equals(findBook.getBookName()) && lendBook.getBookID()==findBook.getBookID()){
                map.put("status",2);
            }
        }
        return map;
    }


    //历史记录借阅
    @RequestMapping("/lendHistory")
    @ResponseBody
    public Map lendHistory(@RequestParam(defaultValue = "1", value = "page")
                                       Integer pageNum,
                           @RequestParam(defaultValue = "5", value = "limit")
                                       Integer pageSize,
                           Reader reader,
                           HttpSession session){

        PageHelper.startPage(pageNum,pageSize);
        Reader attribute = (Reader) session.getAttribute("reader");
        String user = attribute.getName();

        List<LendHistory> list = bookService.lendHistory(user);
        PageInfo<LendHistory> pageInfo = new PageInfo(list);

        HashMap<Object, Object> tableObj = new HashMap<>();
        tableObj.put("code", 0);
        tableObj.put("msg", "");
        tableObj.put("count",pageInfo.getTotal());
        tableObj.put("data",list);

        return tableObj;
    }

    //跳到借阅历史记录
    @RequestMapping("/toHistory")
    public String toBack(){
        return "backBookAdmin";
    }

    @RequestMapping("/lendHistoryAdmin")
    @ResponseBody
    public Map lendHistoryAdmin(@RequestParam(defaultValue = "1", value = "page")
                                   Integer pageNum,
                           @RequestParam(defaultValue = "5", value = "limit")
                                   Integer pageSize){

        PageHelper.startPage(pageNum,pageSize);

        List<LendHistory> list = bookService.queryAll();
        PageInfo<LendHistory> pageInfo = new PageInfo(list);

        HashMap<Object, Object> tableObj = new HashMap<>();
        tableObj.put("code", 0);
        tableObj.put("msg", "");
        tableObj.put("count",pageInfo.getTotal());
        tableObj.put("data",list);

        return tableObj;
    }


    @RequestMapping("/backBook")
    @ResponseBody
    public Map backBook(){
        HashMap<Object, Object> map = new HashMap<>();



        return map;
    }


}
