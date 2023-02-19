package ru.otus.logger.agent;

import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DescriptorHandler {

    public List<MethodArg> getLoadInstructionsForMethodArgs(String methodDescriptor) {
        List<String> args = getArgs(methodDescriptor);
        return args.stream()
                .map(arg -> {
                    int instruction = defineOpcodeForLoadArg(arg);
                    return new MethodArg(arg, instruction);
                })
                .collect(Collectors.toList());
    }

    private int defineOpcodeForLoadArg(String arg) {
        return switch (arg) {
            case "I" -> Opcodes.ILOAD;
            case "J" -> Opcodes.LLOAD;
            case "F" -> Opcodes.FLOAD;
            case "D" -> Opcodes.DLOAD;
            default -> Opcodes.ALOAD;
        };
    }

    private List<String> getArgs(String descriptor) {
        List<String> args = new ArrayList<>();
        Pattern argTypePattern = Pattern.compile("(\\[.+?;)|(L.+?;)|(I)|(J)|(F)|(D)");
        String argsAsString = descriptor.substring(1, descriptor.indexOf(')'));
        Matcher matcher = argTypePattern.matcher(argsAsString);
        while (matcher.find()) {
            args.add(matcher.group());
        }
        return args;
    }

}
