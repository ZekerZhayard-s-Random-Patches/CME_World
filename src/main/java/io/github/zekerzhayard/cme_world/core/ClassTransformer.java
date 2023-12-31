package io.github.zekerzhayard.cme_world.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String className, String transformedName, byte[] basicClass) {
        if ("net.minecraft.world.World".equals(transformedName)) {
            ClassNode cn = new ClassNode();
            new ClassReader(basicClass).accept(cn, ClassReader.EXPAND_FRAMES);
            for (MethodNode mn : cn.methods) {
                if (RemapUtils.checkMethodName(cn.name, mn.name, mn.desc, "<init>") && RemapUtils.checkMethodDesc(mn.desc, "(Lnet/minecraft/world/storage/ISaveHandler;Lnet/minecraft/world/storage/WorldInfo;Lnet/minecraft/world/WorldProvider;Lnet/minecraft/profiler/Profiler;Z)V")) {
                    for (AbstractInsnNode ain : mn.instructions.toArray()) {
                        if (ain.getOpcode() == Opcodes.PUTFIELD) {
                            FieldInsnNode fin = (FieldInsnNode) ain;
                            if (RemapUtils.checkClassName(fin.owner, "net/minecraft/world/World") && RemapUtils.checkFieldName(fin.owner, fin.name, fin.desc, "field_175730_i") && RemapUtils.checkFieldDesc(fin.desc, "Ljava/util/List;")) {
                                mn.instructions.insertBefore(fin, new InsnNode(Opcodes.POP));
                                mn.instructions.insertBefore(fin, new MethodInsnNode(Opcodes.INVOKESTATIC, "io/github/zekerzhayard/cme_world/CopyOnWriteArrayListWithMutableIterator", "create", "()Ljava/util/List;", false));
                            }
                        }
                    }
                } else if (RemapUtils.checkMethodName(cn.name, mn.name, mn.desc, "func_72866_a") && RemapUtils.checkMethodDesc(mn.desc, "(Lnet/minecraft/entity/Entity;Z)V")) {
                    for (AbstractInsnNode ain : mn.instructions.toArray()) {
                        if (ain.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            MethodInsnNode min = (MethodInsnNode) ain;
                            if (RemapUtils.checkClassName(min.owner, "net/minecraft/entity/Entity") && RemapUtils.checkMethodName(min.owner, min.name, min.desc, "func_184188_bt") && RemapUtils.checkMethodDesc(min.desc, "()Ljava/util/List;")) {
                                mn.instructions.insert(min, new MethodInsnNode(Opcodes.INVOKESTATIC, "io/github/zekerzhayard/cme_world/CopyOnWriteArrayListWithMutableIterator", "create", "(Ljava/util/Collection;)Ljava/util/List;", false));
                            }
                        }
                    }
                }
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            basicClass = cw.toByteArray();
        } else if ("net.minecraft.world.chunk.Chunk".equals(transformedName)) {
            ClassNode cn = new ClassNode();
            new ClassReader(basicClass).accept(cn, ClassReader.EXPAND_FRAMES);
            for (MethodNode mn : cn.methods) {
                if (RemapUtils.checkMethodName(cn.name, mn.name, mn.desc, "<init>") && RemapUtils.checkMethodDesc(mn.desc, "(Lnet/minecraft/world/World;II)V")) {
                    for (AbstractInsnNode ain : mn.instructions.toArray()) {
                        if (ain.getOpcode() == Opcodes.PUTFIELD) {
                            FieldInsnNode fin = (FieldInsnNode) ain;
                            if (RemapUtils.checkClassName(fin.owner, "net/minecraft/world/chunk/Chunk") && RemapUtils.checkFieldName(fin.owner, fin.name, fin.desc, "field_150816_i") && RemapUtils.checkFieldDesc(fin.desc, "Ljava/util/Map;")) {
                                mn.instructions.insertBefore(fin, new InsnNode(Opcodes.POP));
                                mn.instructions.insertBefore(fin, new MethodInsnNode(Opcodes.INVOKESTATIC, "io/github/zekerzhayard/cme_world/ConcurrentHashMapWithNullKey", "create", "()Ljava/util/Map;", false));
                            }
                        }
                    }
                }
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            basicClass = cw.toByteArray();
        }
        return basicClass;
    }
}
