package agents;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;


public class TestTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
    	
    	//System.out.println(className);
    	
    	//System.out.println(classLoader.getResource(className));
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        System.out.println("name1: " + cn.name);
        System.out.println("sourceDebug: " + cn.sourceDebug);
        System.out.println("sourceFile: " + cn.sourceFile);
        System.out.println("outerClass: " + cn.outerClass);
        System.out.println("outerMethod: " + cn.outerMethod);
        System.out.println("superName: " + cn.superName);
        System.out.println("outerMethodDesc: " + cn.outerMethodDesc);
        //System.out.println("classBeingRedefined: " + classBeingRedefined.getName());
        
        for (Object obj : cn.methods) {
    		//System.out.println(run.class.getClassLoader().getResource("Tests/run.class"));
            MethodNode md = (MethodNode) obj;
            
            if ("<init>".endsWith(md.name) || "<clinit>".equals(md.name)) {
                continue;
            }
            InsnList insns = md.instructions;
            InsnList il = new InsnList();
            md.visitVarInsn(Opcodes.ALOAD, 0);
            il.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System",
                    "out", "Ljava/io/PrintStream;"));
            il.add(new LdcInsnNode("Enter method-> " + cn.name+"."+md.name));
            il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                    "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
            insns.insert(il);
            md.maxStack += 3;
        }
        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);
        return cw.toByteArray();
    }

}