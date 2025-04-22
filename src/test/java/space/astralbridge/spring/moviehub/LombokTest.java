package space.astralbridge.spring.moviehub;

import lombok.Data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LombokTest {

    @Test
    public void testLombokAnnotations() {
        // 创建一个使用Lombok注解的测试对象
        TestLombokClass testObj = new TestLombokClass();
        
        // 测试setter方法
        testObj.setName("测试名称");
        testObj.setAge(25);
        
        // 测试getter方法
        assertEquals("测试名称", testObj.getName());
        assertEquals(25, testObj.getAge());
        
        // 测试toString方法
        String toString = testObj.toString();
        assertTrue(toString.contains("name=测试名称"));
        assertTrue(toString.contains("age=25"));
        
        // 测试equals和hashCode方法
        TestLombokClass testObj2 = new TestLombokClass();
        testObj2.setName("测试名称");
        testObj2.setAge(25);
        
        assertEquals(testObj, testObj2);
        assertEquals(testObj.hashCode(), testObj2.hashCode());
    }
    
    // 内部测试类，使用Lombok的@Data注解
    @Data
    static class TestLombokClass {
        private String name;
        private int age;
    }
}
