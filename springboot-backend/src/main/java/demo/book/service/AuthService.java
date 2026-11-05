package demo.book.service;
import demo.book.entity.User; import demo.book.mapper.UserMapper; import org.springframework.stereotype.Service; import java.security.MessageDigest; import java.util.*; import java.util.concurrent.ConcurrentHashMap;
/** 演示认证服务：SHA-256 和内存令牌，重启后令牌失效。 */
@Service public class AuthService {
 private final UserMapper mapper; private final Map<String,User> tokens=new ConcurrentHashMap<>();
 public AuthService(UserMapper m){mapper=m;}
 private String hash(String s){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes()));}catch(Exception e){throw new RuntimeException(e);}}
 public Map<String,Object> register(String name,String pass){if(name==null||name.isBlank()||pass==null||pass.length()<4)throw new IllegalArgumentException("用户名必填，密码至少4位"); if(mapper.byName(name)!=null)throw new IllegalArgumentException("用户名已存在"); User u=new User();u.username=name;u.password=hash(pass);mapper.add(u);return login(name,pass);}
 public Map<String,Object> login(String name,String pass){User u=mapper.byName(name);if(u==null||!u.password.equals(hash(pass)))throw new IllegalArgumentException("用户名或密码错误");String t=UUID.randomUUID().toString();tokens.put(t,u);return Map.of("token",t,"user",Map.of("username",u.username,"role",u.role));}
 public User user(String token){User u=tokens.get(token);if(u==null)throw new IllegalArgumentException("请先登录");return u;}
}
