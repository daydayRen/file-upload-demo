package cn.com.intasect.multiple.file.core.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
  * @Author: renyitian
  * @Description:
  * @Date: 11:35 2024/3/18
  */
public class FtpGenericObjectPool extends GenericObjectPool<FTPClient> {

    public FtpGenericObjectPool(PooledObjectFactory<FTPClient> factory) {
        super(factory);
    }

    public FtpGenericObjectPool(PooledObjectFactory<FTPClient> factory, GenericObjectPoolConfig<FTPClient> config) {
        super(factory, config);
    }

    public FtpGenericObjectPool(PooledObjectFactory<FTPClient> factory, GenericObjectPoolConfig<FTPClient> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }

    @Override
    public FTPClient borrowObject() throws Exception {
        return super.borrowObject();
    }

    @Override
    public void returnObject(FTPClient obj) {
        super.returnObject(obj);
    }

}
