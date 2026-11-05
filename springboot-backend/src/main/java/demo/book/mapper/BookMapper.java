package demo.book.mapper;
import demo.book.entity.Book; import org.apache.ibatis.annotations.*; import java.util.*;
/** 图书 SQL 映射。 */
@Mapper public interface BookMapper {
 @Select("<script>SELECT * FROM books WHERE 1=1 <if test='q!=null and q!=\"\"'>AND title LIKE CONCAT('%',#{q},'%')</if><if test='author!=null and author!=\"\"'>AND author LIKE CONCAT('%',#{author},'%')</if><if test='category!=null and category!=\"\"'>AND category=#{category}</if> ORDER BY id DESC LIMIT #{size} OFFSET #{offset}</script>") List<Book> page(@Param("q")String q,@Param("author")String author,@Param("category")String category,@Param("size")int size,@Param("offset")int offset);
 @Select("SELECT COUNT(*) FROM books WHERE (#{q} IS NULL OR title LIKE CONCAT('%',#{q},'%')) AND (#{author} IS NULL OR author LIKE CONCAT('%',#{author},'%')) AND (#{category} IS NULL OR category=#{category})") long count(String q,String author,String category);
 @Select("SELECT * FROM books WHERE id=#{id}") Book get(long id);
 @Insert("INSERT INTO books(title,author,category,summary,cover_url) VALUES(#{title},#{author},#{category},#{summary},#{coverUrl})") @Options(useGeneratedKeys=true,keyProperty="id") int add(Book b);
 @Update("UPDATE books SET title=#{title},author=#{author},category=#{category},summary=#{summary},cover_url=#{coverUrl} WHERE id=#{id}") int edit(Book b);
 @Delete("DELETE FROM books WHERE id=#{id}") int delete(long id);
 @Delete("<script>DELETE FROM books WHERE id IN <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>") int deleteMany(@Param("ids")List<Long> ids);
}
