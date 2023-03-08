package ru.otus.logger.agent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

public class AnnotationHandler extends MethodVisitor {

  private static final String ANNOTATION_CLASS = "Lru/otus/logger/annotation/Log;";
  private static final DescriptorHandler DESCRIPTOR_HANDLER = new DescriptorHandler();

  private static final String LONG_TYPE = "J";
  private static final String DOUBLE_TYPE = "D";

  private final String methodDescriptor;
  private final String methodName;
  private boolean isAnnotationPresent;

  public AnnotationHandler(int api, MethodVisitor methodVisitor, String methodDescriptor, String methodName) {
    super(api, methodVisitor);
    this.methodDescriptor = methodDescriptor;
    this.methodName = methodName;
  }

  @Override
  public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
    if (descriptor.equals(ANNOTATION_CLASS)) {
      isAnnotationPresent = true;
    }
    return super.visitAnnotation(descriptor, visible);
  }

  @Override
  public void visitCode() {
    if (isAnnotationPresent) {
      mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
      mv.visitInsn(Opcodes.DUP);
      int[] index = new int[]{0};
      final List<MethodArg> methodArgs = DESCRIPTOR_HANDLER.getLoadInstructionsForMethodArgs(methodDescriptor);
      addMethodNameToStringBuilder(methodArgs);
      methodArgs.forEach(methodArg -> addParameterToStringBuilder(index, methodArg, methodArgs));
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
    super.visitCode();
  }

  private void addMethodNameToStringBuilder(List<MethodArg> methodArgs) {
    if (methodArgs.isEmpty()) {
      mv.visitLdcInsn(methodName);
    } else {
      mv.visitLdcInsn(methodName + ": ");
    }
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
  }

  private void addParameterToStringBuilder(int[] index, MethodArg methodArg, List<MethodArg> methodArgs) {
    mv.visitLdcInsn("param" + index[0] + ": ");
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
    mv.visitVarInsn(methodArg.instruction(), ++index[0]);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(" + methodArg.type() + ")Ljava/lang/StringBuilder;", false);
    if (index[0] == methodArgs.size()) {
      mv.visitLdcInsn(".");
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
    } else {
      mv.visitLdcInsn(", ");
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
    }
    if (methodArg.type().equals(LONG_TYPE) || methodArg.type().equals(DOUBLE_TYPE)) {
      ++index[0];
    }
  }

}
