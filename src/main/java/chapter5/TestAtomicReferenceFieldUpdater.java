package chapter5;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class TestAtomicReferenceFieldUpdater {
    public static void main(String[] args) {
        Student student = new Student();

        // 获得原子更新器
        // 泛型
        // 参数1 持有属性的类 参数2 被更新的属性的类
        // newUpdater中的参数：第三个为属性的名称
        AtomicReferenceFieldUpdater<Student, String> updater = AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

        // 修改
        updater.compareAndSet(student, null, "Nyima");
        System.out.println(student);
    }
}

class Student {
    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
