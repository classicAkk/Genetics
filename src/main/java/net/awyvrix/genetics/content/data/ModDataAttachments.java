package net.awyvrix.genetics.content.data;

import net.awyvrix.genetics.content.data.custom.PlayerData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.function.Supplier;

import static net.awyvrix.genetics.Genetics.MOD_ID;

public class ModDataAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);

    public static final Supplier<AttachmentType<PlayerData>> PLAYER_DATA =
            ATTACHMENTS.register(
                    "dna_data",
                    () -> AttachmentType.builder(() -> new PlayerData(new HashMap<>(), new HashMap<>()))
                            .serialize(PlayerData.CODEC)
                            .copyOnDeath()
                            .build()
            );
}