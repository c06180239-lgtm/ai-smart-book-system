package demo.book.controller;
import demo.book.entity.*;import demo.book.mapper.*;import demo.book.service.*;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import org.springframework.beans.factory.annotation.Value;import org.springframework.web.client.RestTemplate;import java.nio.file.*;import java.util.*;
/** 聚合演示 REST API；写操作统一校验管理员身份。 */
@RestController @RequestMapping("/api") public class ApiController {
 final BookMapper books;final AuthService auth;final RestTemplate http;@Value("${app.upload-dir}")String upload;@Value("${app.ai-url}")String aiUrl;
 public ApiController(BookMapper b,AuthService a,RestTemplate h){books=b;auth=a;http=h;}
 String token(String h){return h==null?"":h.replace("Bearer ","");} User who(String h){return auth.user(token(h));} void admin(String h){if(!"ADMIN".equals(who(h).role))throw new IllegalArgumentException("仅管理员可操作");}
 @PostMapping("/auth/register") Object register(@RequestBody Map<String,String> m){return auth.register(m.get("username"),m.get("password"));}
 @PostMapping("/auth/login") Object login(@RequestBody Map<String,String> m){return auth.login(m.get("username"),m.get("password"));}
 @GetMapping("/books") Object page(@RequestParam(defaultValue="1")int page,@RequestParam(defaultValue="10")int size,@RequestParam(required=false)String q,@RequestParam(required=false)String author,@RequestParam(required=false)String category){return Map.of("items",books.page(q,author,category,size,(page-1)*size),"total",books.count(q,author,category));}
 @PostMapping("/books") Object add(@RequestHeader("Authorization")String h,@RequestBody Book b){admin(h);books.add(b);return b;}
 @PutMapping("/books/{id}") Object edit(@RequestHeader("Authorization")String h,@PathVariable long id,@RequestBody Book b){admin(h);b.id=id;books.edit(b);return b;}
 @DeleteMapping("/books/{id}") Object del(@RequestHeader("Authorization")String h,@PathVariable long id){admin(h);return Map.of("deleted",books.delete(id));}
 @PostMapping("/books/batch-delete") Object batch(@RequestHeader("Authorization")String h,@RequestBody Map<String,List<Long>> m){admin(h);return Map.of("deleted",books.deleteMany(m.get("ids")));}
 @PostMapping("/upload") Object upload(@RequestHeader("Authorization")String h,@RequestParam MultipartFile file)throws Exception{admin(h);String n=UUID.randomUUID()+"-"+Paths.get(file.getOriginalFilename()).getFileName();Path d=Path.of(upload);Files.createDirectories(d);file.transferTo(d.resolve(n));return Map.of("url","/uploads/"+n);}
 @GetMapping("/uploads/{name}") org.springframework.core.io.Resource image(@PathVariable String name)throws Exception{return new org.springframework.core.io.UrlResource(Path.of(upload).resolve(Paths.get(name).getFileName()).toUri());}
 @PostMapping("/ai/{action}") Object ai(@RequestHeader("Authorization")String h,@PathVariable String action,@RequestBody Map<String,Object> body){who(h);return http.postForObject(aiUrl+"/api/ai/"+action,body,Map.class);}
 @ExceptionHandler(Exception.class) @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST) Object error(Exception e){return Map.of("error",e.getMessage()==null?"request failed":e.getMessage());}
}
