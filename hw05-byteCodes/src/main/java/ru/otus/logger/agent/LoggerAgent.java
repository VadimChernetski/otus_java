package ru.otus.logger.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class LoggerAgent {

  public static void premain(String agentArgs, Instrumentation inst) {
    inst.addTransformer(new ClassFileTransformer() {
      @Override
      public byte[] transform(ClassLoader loader,
                              String className, Class<?> classBeingRedefined,
                              ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        return addLogging(classfileBuffer);
      }
    });
  }

  private static byte[] addLogging(byte[] originalClass) {
    var classReader = new ClassReader(originalClass);
    var classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
    final ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {

      @Override
      public MethodVisitor visitMethod(int access, String name, String methodDescriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, methodDescriptor, signature, exceptions);
        return new AnnotationHandler(Opcodes.ASM5, methodVisitor, methodDescriptor, name);
      }
    };

    classReader.accept(classVisitor, Opcodes.ASM5);

    return classWriter.toByteArray();
  }

}
