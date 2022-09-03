package chapter5;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class GetUnsafe {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        // 通过反射获得Unsafe对象
        Class unsafeClass = Unsafe.class;
        // 获得构造函数，Unsafe的构造函数为私有的
        Constructor constructor = unsafeClass.getDeclaredConstructor();
        // 设置为允许访问私有内容
        constructor.setAccessible(true);
        // 创建Unsafe对象
        Unsafe unsafe = (Unsafe) constructor.newInstance();

        // 创建Person对象
        Person person = new Person();
        // 获得其属性 name 的偏移量
        Field field = Person.class.getDeclaredField("name");
        long offset = unsafe.objectFieldOffset(field);

        // 通过unsafe的CAS操作改变值
        unsafe.compareAndSwapObject(person, offset, null, "Tim");
        System.out.println(person);
    }
}

class Person {
    // 配合CAS操作，必须用volatile修饰
    volatile String name;


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}