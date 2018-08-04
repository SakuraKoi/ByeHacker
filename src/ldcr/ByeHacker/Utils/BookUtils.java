package ldcr.ByeHacker.Utils;

import static ldcr.lib.org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static ldcr.lib.org.objectweb.asm.Opcodes.ACC_STATIC;
import static ldcr.lib.org.objectweb.asm.Opcodes.ACC_SUPER;
import static ldcr.lib.org.objectweb.asm.Opcodes.ALOAD;
import static ldcr.lib.org.objectweb.asm.Opcodes.ASTORE;
import static ldcr.lib.org.objectweb.asm.Opcodes.CHECKCAST;
import static ldcr.lib.org.objectweb.asm.Opcodes.DUP;
import static ldcr.lib.org.objectweb.asm.Opcodes.GETFIELD;
import static ldcr.lib.org.objectweb.asm.Opcodes.ILOAD;
import static ldcr.lib.org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static ldcr.lib.org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static ldcr.lib.org.objectweb.asm.Opcodes.INVOKESTATIC;
import static ldcr.lib.org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static ldcr.lib.org.objectweb.asm.Opcodes.ISTORE;
import static ldcr.lib.org.objectweb.asm.Opcodes.NEW;
import static ldcr.lib.org.objectweb.asm.Opcodes.POP;
import static ldcr.lib.org.objectweb.asm.Opcodes.RETURN;
import static ldcr.lib.org.objectweb.asm.Opcodes.V1_7;

import java.io.File;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import ldcr.ByeHacker.impl.IBookUtils;
import ldcr.LdcrUtils.internal.ImplLoader;
import ldcr.LdcrUtils.plugin.LdcrUtils;
import ldcr.Utils.FileUtils;
import ldcr.lib.org.objectweb.asm.ClassWriter;
import ldcr.lib.org.objectweb.asm.Label;
import ldcr.lib.org.objectweb.asm.MethodVisitor;

public class BookUtils {
	private static IBookUtils impl;
	public static void openBook(final Player player, final ItemStack book) {
		impl.openBook(player, book);
	}
	public static void addBookPage(final BookMeta book, final String json) {
		impl.addBookPage(book, json);
	}
	public static void loadImpl() {
		final File implFile = ImplLoader.getInstance().getImplFile("ByeHacker-BookUtils");
		byte[] bytecode;
		if (implFile.exists()) {
			try {
				bytecode = FileUtils.readFileByte(implFile);
			} catch (final IOException e) {
				e.printStackTrace();
				bytecode = newImpl();
				try {
					FileUtils.writeFileByte(implFile, bytecode);
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			bytecode = newImpl();
			try {
				FileUtils.writeFileByte(implFile, bytecode);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
		impl = (IBookUtils) ImplLoader.getInstance().newInstance(bytecode, "ldcr.ByeHacker.impl.BookUtilsImpl");
	}
	private static byte[] newImpl() {
		final String nmsVersion = LdcrUtils.getNmsVersion();
		final ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;

		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "ldcr/ByeHacker/impl/BookUtilsImpl", null, "java/lang/Object", new String[] { "ldcr/ByeHacker/impl/IBookUtils" });

		cw.visitSource("BookUtilsImpl2.java", null);

		cw.visitInnerClass("net/minecraft/server/"+nmsVersion+"/IChatBaseComponent$ChatSerializer", "net/minecraft/server/"+nmsVersion+"/IChatBaseComponent", "ChatSerializer", ACC_PUBLIC + ACC_STATIC);

		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			final Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(14, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			mv.visitInsn(RETURN);
			final Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lldcr/ByeHacker/impl/BookUtilsImpl2;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "openBook", "(Lorg/bukkit/entity/Player;Lorg/bukkit/inventory/ItemStack;)V", null, null);
			mv.visitCode();
			final Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(18, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "getInventory", "()Lorg/bukkit/inventory/PlayerInventory;", true);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/inventory/PlayerInventory", "getHeldItemSlot", "()I", true);
			mv.visitVarInsn(ISTORE, 3);
			final Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(19, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "getInventory", "()Lorg/bukkit/inventory/PlayerInventory;", true);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/inventory/PlayerInventory", "getItem", "(I)Lorg/bukkit/inventory/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 4);
			final Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(20, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "getInventory", "()Lorg/bukkit/inventory/PlayerInventory;", true);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/inventory/PlayerInventory", "setItem", "(ILorg/bukkit/inventory/ItemStack;)V", true);
			final Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(21, l3);
			mv.visitTypeInsn(NEW, "net/minecraft/server/"+nmsVersion+"/PacketPlayOutCustomPayload");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("MC|BOpen");
			mv.visitTypeInsn(NEW, "net/minecraft/server/"+nmsVersion+"/PacketDataSerializer");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESTATIC, "io/netty/buffer/Unpooled", "buffer", "()Lio/netty/buffer/ByteBuf;", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/"+nmsVersion+"/PacketDataSerializer", "<init>", "(Lio/netty/buffer/ByteBuf;)V", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/"+nmsVersion+"/PacketPlayOutCustomPayload", "<init>", "(Ljava/lang/String;Lnet/minecraft/server/"+nmsVersion+"/PacketDataSerializer;)V", false);
			mv.visitVarInsn(ASTORE, 5);
			final Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(22, l4);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, "org/bukkit/craftbukkit/"+nmsVersion+"/entity/CraftPlayer");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/craftbukkit/"+nmsVersion+"/entity/CraftPlayer", "getHandle", "()Lnet/minecraft/server/"+nmsVersion+"/EntityPlayer;", false);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/server/"+nmsVersion+"/EntityPlayer", "playerConnection", "Lnet/minecraft/server/"+nmsVersion+"/PlayerConnection;");
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/"+nmsVersion+"/PlayerConnection", "sendPacket", "(Lnet/minecraft/server/"+nmsVersion+"/Packet;)V", false);
			final Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(23, l5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "getInventory", "()Lorg/bukkit/inventory/PlayerInventory;", true);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/inventory/PlayerInventory", "setItem", "(ILorg/bukkit/inventory/ItemStack;)V", true);
			final Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(24, l6);
			mv.visitInsn(RETURN);
			final Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("this", "Lldcr/ByeHacker/impl/BookUtilsImpl2;", null, l0, l7, 0);
			mv.visitLocalVariable("player", "Lorg/bukkit/entity/Player;", null, l0, l7, 1);
			mv.visitLocalVariable("book", "Lorg/bukkit/inventory/ItemStack;", null, l0, l7, 2);
			mv.visitLocalVariable("slot", "I", null, l1, l7, 3);
			mv.visitLocalVariable("old", "Lorg/bukkit/inventory/ItemStack;", null, l2, l7, 4);
			mv.visitLocalVariable("packet", "Lnet/minecraft/server/"+nmsVersion+"/PacketPlayOutCustomPayload;", null, l4, l7, 5);
			mv.visitMaxs(6, 6);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "addBookPage", "(Lorg/bukkit/inventory/meta/BookMeta;Ljava/lang/String;)V", null, null);
			mv.visitCode();
			final Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(28, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, "org/bukkit/craftbukkit/"+nmsVersion+"/inventory/CraftMetaBook");
			mv.visitVarInsn(ASTORE, 3);
			final Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(29, l1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/server/"+nmsVersion+"/IChatBaseComponent$ChatSerializer", "a", "(Ljava/lang/String;)Lnet/minecraft/server/"+nmsVersion+"/IChatBaseComponent;", false);
			mv.visitVarInsn(ASTORE, 4);
			final Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(30, l2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(GETFIELD, "org/bukkit/craftbukkit/"+nmsVersion+"/inventory/CraftMetaBook", "pages", "Ljava/util/List;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			final Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(31, l3);
			mv.visitInsn(RETURN);
			final Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("this", "Lldcr/ByeHacker/impl/BookUtilsImpl2;", null, l0, l4, 0);
			mv.visitLocalVariable("meta", "Lorg/bukkit/inventory/meta/BookMeta;", null, l0, l4, 1);
			mv.visitLocalVariable("json", "Ljava/lang/String;", null, l0, l4, 2);
			mv.visitLocalVariable("craftMetaBook", "Lorg/bukkit/craftbukkit/"+nmsVersion+"/inventory/CraftMetaBook;", null, l1, l4, 3);
			mv.visitLocalVariable("component", "Lnet/minecraft/server/"+nmsVersion+"/IChatBaseComponent;", null, l2, l4, 4);
			mv.visitMaxs(2, 5);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}
