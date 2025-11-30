import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<User> list = new ArrayList<>();
        ArrayList<Student> studentList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        loop:
        while (true) {
            System.out.println("欢迎来到学生管理系统");
            System.out.println("请选择操作：\t 1.登录 \t 2.注册 \t 3.忘记密码 \t 4.退出");

            String choose = sc.nextLine();
            switch (choose) {
                case "1" -> login(list, studentList);
                case "2" -> signup(list);
                case "3" -> forgetPassword(list);
                case "4" -> {
                    System.out.println("退出");
                    break loop;
                }
                default -> System.out.println("无效选项，请重新输入");
            }
        }


    }

    public static void login(ArrayList<User> list, ArrayList<Student> studentList) {
        //列表为空提示先注册并直接退出
        if (list.isEmpty()) {
            System.out.println("当前没有任何已注册用户，请先注册账号。");
            return;
        }
        System.out.println("请输入用户名：");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();

        while (userNameLocate(list, userName) == -1) {
            System.out.println("该用户名不存在，请重新输入：");
            userName = sc.nextLine();
        }
        User user = list.get(userNameLocate(list, userName));
        //统计输入错误密码次数
        int tryTimes = 0;

        //System.out.println("请输入密码：");
        //String passWord = sc.nextLine()();

        //只要输入错误次数不为3就重复，输入错误验证码不增加输入错误次数
        loop:
        while (tryTimes < 3) {
            System.out.println("请输入密码：");
            String passWord = sc.nextLine();
            String code = generateCode();
            System.out.println("请输入如下验证码：" + code);
            String enteredCode = sc.nextLine();

            //输入错误验证码后重新生成新的验证码但保留原来输入的密码
            while (!enteredCode.equals(code)) {
                code = generateCode();
                System.out.println("验证码输入错误，请重新输入如下验证码：" + code);
                enteredCode = sc.nextLine();
            }

            String verifyPassWord = user.getUserPassword();

            //在验证码正确后开始判断密码是否正确
            if (!passWord.equals(verifyPassWord)) {
                //密码错误后增加计数器，并优先验证是否错误3次，仅不为3次才能重新输入密码
                tryTimes++;
                if (tryTimes == 3) {
                    System.out.println("已输入错误密码3次，请重新登录");
                    break loop;
                }
                //先提示剩余次数随后退回至输入密码处，需要再重新输入密码和验证验证码
                System.out.println("输入密码有误，请重新输入，还剩" + (3 - tryTimes) + "次机会");
                //passWord = sc.nextLine()();
            } else {
                //正确后进入学生系统，同时退出登录功能回到主界面
                studentManagementSystem(studentList);
                return;
            }
        }
    }

    public static void signup(ArrayList<User> list) {
        System.out.println("请输入用户名，用户名长度请在3-15个数字和字母字符之间不能由纯数字组成");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();

        while (userNameLocate(list, userName) != -1) {
            System.out.println("该用户名已存在，请重新输入：");
            userName = sc.nextLine();
        }
        //根据不同错误类型打印提示并在重新输入后回到循环，每个case重新输入后都优先单独检查重复性避免在输入正确格式但是是重复用户名时没能检查出来
        loop:
        while (true) {
            int choose = userNameFormatCheck(userName);
            switch (choose) {
                //case1 长度不在3-15范围内
                case 1 -> {
                    System.out.println("用户名长度需在3-15个数字和字母字符之间，请重新输入：");
                    userName = sc.nextLine();
                    if (userNameLocate(list, userName) != -1) {
                        System.out.println("该用户名已存在，请重新输入：");
                        continue;  // 再次进入 loop，重新检查格式
                    }
                }
                //case2 包含除数字字母以外字符
                case 2 -> {
                    System.out.println("用户名不能由数字和字母以外的字符组成，请重新输入：");
                    userName = sc.nextLine();
                    if (userNameLocate(list, userName) != -1) {
                        System.out.println("该用户名已存在，请重新输入：");
                        continue;  // 再次进入 loop，重新检查格式
                    }
                }
                //case3 字母个数为0
                case 3 -> {
                    System.out.println("用户名不能由纯数字组成，请重新输入：");
                    userName = sc.nextLine();
                    if (userNameLocate(list, userName) != -1) {
                        System.out.println("该用户名已存在，请重新输入：");
                        continue;  // 再次进入 loop，重新检查格式
                    }
                }
                case 4 -> {
                    break loop;
                }
            }
        }

        System.out.println("请输入密码：");
        String userPassWord1 = sc.nextLine();
        System.out.println("请再次输入密码：");
        String userPassWord2 = sc.nextLine();

        //输入两次密码后开始对比
        loop2:
        while (true) {
            if (userPassWord1.equals(userPassWord2)) {
                break loop2;
            } else {
                System.out.println("两次密码不一致，请重新输入");
                System.out.println("请输入密码：");
                userPassWord1 = sc.nextLine();
                System.out.println("请再次输入密码：");
                userPassWord2 = sc.nextLine();
            }
        }

        System.out.println("请输入身份证号码:");
        String userId = sc.nextLine();

        //根据不同错误类型打印提示并在重新输入后回到循环
        loop3:
        while (true) {
            int choose = userIdFormatCheck(userId);
            switch (choose) {
                //case1 长度不为18位
                case 1 -> {
                    System.out.println("未输入身份证号码或长度不为18位，请重新输入：");
                    userId = sc.nextLine();
                }
                //case2 由0开头
                case 2 -> {
                    System.out.println("身份证号码不能由0开头，请重新输入：");
                    userId = sc.nextLine();
                }
                //case3 在前17位中含有数字以外的字符或最后一位为数字和大小写X以外的字符
                case 3 -> {
                    System.out.println("身份证号码需为数字仅最后一位可为大小写X，不能使用其他字母或字符，请重新输入：");
                    userId = sc.nextLine();
                }
                case 4 -> {
                    break loop3;
                }
            }
        }

        System.out.println("请输入手机号:");
        String userPhNumber = sc.nextLine();

        //根据不同错误类型打印提示并在重新输入后回到循环
        loop4:
        while (true) {
            int choose = userPhoneNumberFormatCheck(userPhNumber);
            switch (choose) {
                //case1 长度不为11位
                case 1 -> {
                    System.out.println("未输入手机号或长度不为11位，请重新输入：");
                    userPhNumber = sc.nextLine();
                }
                //case2 由0开头
                case 2 -> {
                    System.out.println("手机号不能由0开头，请重新输入：");
                    userPhNumber = sc.nextLine();
                }
                //case3 含有数字以外的字符
                case 3 -> {
                    System.out.println("手机号仅能为数字，请重新输入：");
                    userPhNumber = sc.nextLine();
                }
                case 4 -> {
                    break loop4;
                }
            }
        }

        User user = new User(userName, userPassWord1, userId, userPhNumber);
        list.add(user);
        //注册成功后提示账号完整信息
        System.out.println("账号已成功注册：" + "\t" + "用户名：" + userName + "\t" + "账号密码：" + userPassWord1 + "\t" + "身份证号：" + userId + "\t" + "手机号：" + userPhNumber);

    }

    public static void forgetPassword(ArrayList<User> list) {
        //列表为空提示先注册并直接退出
        if (list.isEmpty()) {
            System.out.println("当前没有任何已注册用户，请先注册账号。");
            return;
        }

        System.out.println("请输入用户名：");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();

        while (userNameLocate(list, userName) == -1) {
            System.out.println("该用户名不存在，请重新输入：");
            userName = sc.nextLine();
        }
        User user = list.get(userNameLocate(list, userName));
        String id = user.getUserId();
        String phoneNumber = user.getUserPhoneNumber();

        System.out.println("请输入身份证号：");
        String enteredId = sc.nextLine();
        System.out.println("请输入手机号：");
        String enteredPhoneNumber = sc.nextLine();

        if (id.equals(enteredId) && phoneNumber.equals(enteredPhoneNumber)) {
            System.out.println("验证成功，请输入新的密码：");
            String newPassWord = sc.nextLine();
            user.setUserPassword(newPassWord);
            //System.out.println("密码修改成功");
            System.out.println("密码修改成功：" + "\t" + "用户名：" + userName + "\t" + "账号密码：" + newPassWord + "\t" + "身份证号：" + id + "\t" + "手机号：" + phoneNumber);
            return;
        } else {
            System.out.println("验证失败，无法修改密码");
            return;
        }
    }

    public static int userNameLocate(ArrayList<User> list, String userName) {
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (userName.equals(user.getUserName())) {
                return i;
            }
        }
        return -1;
    }

    public static int userNameFormatCheck(String userName) {
        //case 1 长度范围不符
        if (userName.length() < 3 || userName.length() > 15) {
            return 1;
        }
        int count = 0;
        char[] name = userName.toCharArray();
        //case 2 有不是数字和大小写字母的字符
        for (int i = 0; i < name.length; i++) {
            boolean isDigit = (name[i] >= '0' && name[i] <= '9');
            boolean isLower = (name[i] >= 'a' && name[i] <= 'z');
            boolean isUpper = (name[i] >= 'A' && name[i] <= 'Z');
            if (!isDigit && !isLower && !isUpper) {
                return 2;
            }
            if (isLower || isUpper) {
                count++;
            }
        }
        //case 3 没有任何字母
        if (count == 0) {
            return 3;
        }
        //case 4 符合需求
        return 4;
    }

    public static int userIdFormatCheck(String userId) {
        //case1 长度不为18位
        if (userId == null || userId.length() != 18) {
            return 1;
        }
        char[] id = userId.toCharArray();
        //case2 由0开头
        if (id[0] == '0') {
            return 2;
        }
        //case3.1 在前17位中含有数字以外的字符
        for (int i = 0; i < 17; i++) {
            boolean isDigit = (id[i] >= '0' && id[i] <= '9');
            if (!isDigit) {
                return 3;
            }
        }
        //case3.2 18位为数字和大小写X以外的字符
        char last = id[17];
        boolean isDigitLast = (last >= '0' && last <= '9');
        if (!(isDigitLast || last == 'X' || last == 'x')) {
            return 3;
        }

        return 4;
    }

    public static int userPhoneNumberFormatCheck(String userPhNumber) {
        //case1 长度不为11位
        if (userPhNumber == null || userPhNumber.length() != 11) {
            return 1;
        }
        char[] phoneNumber = userPhNumber.toCharArray();
        //case2 由0开头
        if (phoneNumber[0] == '0') {
            return 2;
        }
        //case3 含有数字以外的字符
        for (int i = 0; i < phoneNumber.length; i++) {
            boolean isDigit = (phoneNumber[i] >= '0' && phoneNumber[i] <= '9');
            if (!isDigit) {
                return 3;
            }
        }

        return 4;
    }

    public static String generateCode() {
        char[] verifyCode = new char[5];
        Random random = new Random();

        //前四位生成随机字母由0-1随机数判断大小写和0-26判断为什么字母
        for (int i = 0; i < verifyCode.length - 1; i++) {
            int randomChar = random.nextInt(26);
            int upperOrLower = random.nextInt(2);
            if (upperOrLower == 0) {
                verifyCode[i] = (char) ('a' + randomChar);
            } else {
                verifyCode[i] = (char) ('A' + randomChar);
            }
        }
        //最后一位为随机数字
        verifyCode[4] = (char) ('0' + random.nextInt(10));

        //随机交换顺序
        for (int i = verifyCode.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = verifyCode[i];
            verifyCode[i] = verifyCode[j];
            verifyCode[j] = temp;
        }
        return new String(verifyCode);
    }

    //-------------------------------------------------------------------------
    //如下为学生管理系统代码
    public static void studentManagementSystem(ArrayList<Student> list) {
        //ArrayList<Student> list = new ArrayList<>();
        loop:
        while (true) {
            System.out.println("------------欢迎使用学生系统----------");
            System.out.println("1.添加学生信息");
            System.out.println("2.删除学生信息");
            System.out.println("3.修改学生信息");
            System.out.println("4.查询学生信息");
            System.out.println("5.退出");
            System.out.println("请输入您的选择：");

            Scanner sc = new Scanner(System.in);
            String choose = sc.nextLine();
            switch (choose) {
                case "1" -> addStudent(list);
                case "2" -> deleteStudent(list);
                case "3" -> changeStudent(list);
                case "4" -> lookupStudent(list);
                case "5" -> {
                    System.out.println("退出");
                    break loop;
                }
                default -> System.out.println("无效选项，请重新输入");
            }
        }
    }

    //添加学生信息，键盘录入编号姓名年龄地址，如编号与已有学生信息重复则需要重新输入
    public static void addStudent(ArrayList<Student> list) {
        System.out.println("添加学生信息");
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入学生编号：");
        String id = sc.nextLine();
        while (idLocate(list, id) != -1) {
            System.out.println("该编号已存在，请重新输入学生编号：");
            id = sc.nextLine();
        }
        System.out.println("请输入学生姓名：");
        String name = sc.nextLine();
        System.out.println("请输入学生年龄：");
        //int age = sc.nextInt();
        int age;
        while (true) {
            String ageStr = sc.nextLine();
            try {
                age = Integer.parseInt(ageStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("年龄必须是数字，请重新输入：");
            }
        }
        System.out.println("请输入学生地址：");
        String address = sc.nextLine();
        Student stu = new Student(name, id, age, address);
        list.add(stu);
        System.out.println("成功添加学生：" + stu.getId() + "\t" + stu.getName() + "\t" + stu.getAge() + "\t" + stu.getAddress());
    }

    //根据学生id删除学生信息，如没有查到对应编号则重新输入或退出
    public static void deleteStudent(ArrayList<Student> list) {
        System.out.println("删除学生信息");
        System.out.println("请输入需要删除的学生编号：");
        Scanner sc = new Scanner(System.in);
        String deleteId = sc.nextLine();
        if (deleteId.isEmpty()) {
            return;
        }
        while (idLocate(list, deleteId) == -1) {
            System.out.println("没有此编号学生，请重新输入或按回车退出：");
            deleteId = sc.nextLine();
            if (deleteId.isEmpty()) {
                return;
            }
        }
        list.remove(idLocate(list, deleteId));
        System.out.println("学生信息已删除成功");

    }

    public static void changeStudent(ArrayList<Student> list) {
        System.out.println("修改学生信息");
        System.out.println("请输入需要修改的学生编号：");
        Scanner sc = new Scanner(System.in);
        String changeId = sc.nextLine();
        if (changeId.isEmpty()) {
            return;
        }
        while (idLocate(list, changeId) == -1) {
            System.out.println("没有此编号学生，请重新输入或按回车退出：");
            changeId = sc.nextLine();
            if (changeId.isEmpty()) {
                return;
            }
        }
        Student stuModify = list.get(idLocate(list, changeId));
        System.out.println("原学生信息为：" + stuModify.getId() + "\t" + stuModify.getName() + "\t" + stuModify.getAge() + "\t" + stuModify.getAddress());
        System.out.println("请输入新的学生姓名或按回车跳过：");
        String newName = sc.nextLine();
        System.out.println("请输入新的学生年龄或按回车跳过：");
        String newAgeStr = sc.nextLine();
        System.out.println("请输入新的学生地址或按回车跳过：");
        String newAddress = sc.nextLine();

        if (!newName.isEmpty()) {
            stuModify.setName(newName);
        }
        if (!newAgeStr.isEmpty()) {
            try {
                int newAge = Integer.parseInt(newAgeStr);
                stuModify.setAge(newAge);
            } catch (NumberFormatException e) {
                System.out.println("年龄必须是数字，本次年龄修改已忽略。");
            }
        }
        if (!newAddress.isEmpty()) {
            stuModify.setAddress(newAddress);
        }

        System.out.println("成功修改学生信息：" + stuModify.getId() + "\t" + stuModify.getName() + "\t" + stuModify.getAge() + "\t" + stuModify.getAddress());

    }

    public static void lookupStudent(ArrayList<Student> list) {
        System.out.println("查询学生信息");
        if (list.isEmpty()) {
            System.out.println("暂无学生信息可供查询");
            return;
        }
        System.out.println("id\t\t姓名\t年龄\t地址");
        for (Student stu : list) {
            System.out.println(stu.getId() + "\t\t" + stu.getName() + "\t" + stu.getAge() + "\t" + stu.getAddress());
        }
    }

    //根据学生id寻找学生信息在array中的位置，返回找到的位置，没找到返回-1
    public static int idLocate(ArrayList<Student> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            Student stu = list.get(i);
            if (id.equals(stu.getId())) {
                return i;
            }
        }
        return -1;
    }
}