package space.astralbridge.spring.moviehub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import space.astralbridge.spring.moviehub.dto.AddCommentRequest;
import space.astralbridge.spring.moviehub.entity.Comment;
import space.astralbridge.spring.moviehub.entity.User;
import space.astralbridge.spring.moviehub.mapper.CommentMapper;
import space.astralbridge.spring.moviehub.mapper.UserMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CommentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper; // 用于 JSON 转换

    @Autowired
    private CommentMapper commentMapper; // 用于直接操作数据库进行测试准备和验证

    @Autowired
    private UserMapper userMapper; // 用于获取测试用户的 ID

    private MockMvc mockMvc;

    private Long testMovieId = 1L;
    private Long user1Id;
    private Long adminId;
    private Long user1CommentId;
    private Long adminCommentId;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        User user1 = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("username", "user1"));
        User admin = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("username", "admin"));
        assertNotNull(user1, "测试用户 'user1' 不存在");
        assertNotNull(admin, "测试用户 'admin' 不存在");
        user1Id = user1.getId();
        adminId = admin.getId();

        Comment user1Comment = new Comment(user1Id, testMovieId, "User1 的测试评论");
        commentMapper.insert(user1Comment);
        user1CommentId = user1Comment.getId();
        assertNotNull(user1CommentId, "未能插入 User1 的评论");


        Comment adminComment = new Comment(adminId, testMovieId, "Admin 的测试评论");
        commentMapper.insert(adminComment);
        adminCommentId = adminComment.getId();
        assertNotNull(adminCommentId, "未能插入 Admin 的评论");

    }

    //测试获取指定电影的评论列表（分页）是否成功，并且返回的数据格式和内容符合预期（包含之前添加的评论）
    @Test
    public void testGetCommentsByMovie_Success() throws Exception {
        mockMvc.perform(get("/api/comments/movie/{movieId}", testMovieId)
                        .param("current", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.records", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$.data.records[?(@.userId == " + user1Id + ")]").exists())
                .andExpect(jsonPath("$.data.records[?(@.userId == " + adminId + ")]").exists());
    }

    //模拟普通用户 (user1) 登录，测试添加评论的功能是否成功，并验证返回的评论数据是否正确（用户ID、电影ID、内容）
    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testAddComment_AsUser_Success() throws Exception {
        AddCommentRequest request = new AddCommentRequest();
        request.setMovieId(testMovieId);
        request.setContent("这是 user1 添加的新评论");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.userId", is(user1Id.intValue()))) // 验证评论的用户 ID
                .andExpect(jsonPath("$.data.movieId", is(testMovieId.intValue()))) // 验证电影 ID
                .andExpect(jsonPath("$.data.content", is("这是 user1 添加的新评论")));
    }

    //模拟普通用户 (user1) 登录，测试删除自己发布的评论是否成功，并验证数据库中该评论确实被删除了
    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testDeleteOwnComment_AsUser_Success() throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", user1CommentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)));

        assertNull(commentMapper.selectById(user1CommentId));
    }

    //模拟管理员 (admin) 登录，测试删除其他用户 (user1) 的评论是否成功，并验证数据库中该评论确实被删除了
    @Test
    @WithMockUser(username = "user1", roles = {"USER"}) // 模拟普通用户 user1 登录
    public void testDeleteOtherUserComment_AsUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", adminCommentId))
                .andExpect(status().isOk()) // 控制器返回 Result，所以状态码是 200 OK
                .andExpect(jsonPath("$.code", is(403))) // 但业务代码是 403 Forbidden
                .andExpect(jsonPath("$.message", is("无权删除此评论")));
        assertNotNull(commentMapper.selectById(adminCommentId));
    }

    //模拟管理员 (admin) 登录，测试删除自己发布的评论是否成功，并验证数据库中该评论确实被删除了
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // 模拟管理员登录
    public void testDeleteOtherUserComment_AsAdmin_Success() throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", user1CommentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)));
        assertNull(commentMapper.selectById(user1CommentId));
    }

    //模拟未登录用户尝试添加评论，预期请求会被 Spring Security 拦截并返回 403 Forbidden
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // 模拟管理员登录
    public void testDeleteOwnComment_AsAdmin_Success() throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", adminCommentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)));
        assertNull(commentMapper.selectById(adminCommentId));
    }

    //模拟未登录用户尝试删除评论，预期请求会被 Spring Security 拦截并返回 403 Forbidden
    @Test
    public void testAddComment_NotAuthenticated_Fail() throws Exception {
        AddCommentRequest request = new AddCommentRequest();
        request.setMovieId(testMovieId);
        request.setContent("未登录用户尝试添加评论");

        // 不使用 @WithMockUser，模拟未登录状态
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    //模拟用户 (user1) 尝试删除一个不存在的评论 ID，根据之前的逻辑
    @Test
    public void testDeleteComment_NotAuthenticated_Fail() throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", user1CommentId))
                .andExpect(status().isForbidden()); // Spring Security 会拦截并返回 403 Forbidden
    }

    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    public void testDeleteNonExistentComment() throws Exception {
        Long nonExistentCommentId = 99999L;
        mockMvc.perform(delete("/api/comments/{commentId}", nonExistentCommentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200)));
    }
}