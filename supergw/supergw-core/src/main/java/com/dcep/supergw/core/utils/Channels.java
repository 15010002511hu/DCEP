package com.dcep.supergw.core.utils;

import com.dcep.supergw.core.channel.AbstractChannel;
import java.util.Set;

public interface Channels {
	
	static AbstractChannel getChannelByName(Set<AbstractChannel> channels, String name) throws Exception {
        for (AbstractChannel channel : channels) {
            if (channel.getChannelName().contains(name)) {
                return channel;
            }
        }
        throw new Exception("channel[" + name + "]配置错误，找不到处理类");
	}
	
	static AbstractChannel getChannelByClass(Set<AbstractChannel> channels, String className) throws Exception {
        for (AbstractChannel channel : channels) {
            if (channel.getClass().getName().contains(className)) {
                return channel;
            }
        }
        throw new Exception("channel[" + className + "]配置错误，找不到处理类");
	}
	
	static AbstractChannel getChannelByClass(Set<AbstractChannel> channels, Class<?> clazz) throws Exception {
        return getChannelByClass(channels, clazz.getName());
	}
}
