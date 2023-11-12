package com.icegreen.greenmail.configuration;

import com.icegreen.greenmail.base.GreenMailOperations;
import com.icegreen.greenmail.store.FolderException;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * A version of GreenMailOperations that implements the configure() method.
 */
public abstract class ConfiguredGreenMail implements GreenMailOperations {
    private GreenMailConfiguration config;

    @Override
    public ConfiguredGreenMail withConfiguration(GreenMailConfiguration config) {
        this.config = config;
        return this;
    }

    /**
     * This method can be used by child classes to apply the configuration that is stored in config.
     */
    protected void doConfigure() {
        if (config != null) {
            for (UserBean user : config.getUsersToCreate()) {
                setUser(user.getEmail(), user.getLogin(), user.getPassword());
            }
            getUserManager().setAuthRequired(!config.isAuthenticationDisabled());
            getUserManager().setSieveIgnoreDetail(config.isSieveIgnoreDetailEnabled());
            if(config.hasPreloadDir()) {
                try {
                    loadEmails(Paths.get(config.getPreloadDir()));
                } catch (IOException | FolderException e) {
                    throw new IllegalArgumentException("Can not preload emails", e);
                }
            }
        }
    }
}
