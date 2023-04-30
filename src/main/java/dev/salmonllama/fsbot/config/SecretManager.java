/*
 * Copyright (c) 2021 Aleksei Gryczewski
 */

package dev.salmonllama.fsbot.config;

import java.io.IOException;

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;

public enum SecretManager {
    DISCORD_TOKEN   ("projects/fashionscapers-212707/secrets/fs_discord_token/versions/latest"),
    DISCORD_TOKEN_PROD ("projects/fashionscapers-212707/secrets/fs_discord_token_prod/versions/latest"),
    IMGUR_ID        ("projects/fashionscapers-212707/secrets/fs_imgur_client_id/versions/latest"),
    IMGUR_BEARER    ("projects/fashionscapers-212707/secrets/fs_imgur_bearer_token/versions/latest")
    ;

    private final String resourceId;

    SecretManager(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getPlainText() {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            return client.accessSecretVersion(this.resourceId).getPayload().getData().toStringUtf8();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: Add plain text error message to log to console
            System.exit(1); // Secrets are integral to full operation, crash if not retrieved properly.
            return null;
        }
    }
}
