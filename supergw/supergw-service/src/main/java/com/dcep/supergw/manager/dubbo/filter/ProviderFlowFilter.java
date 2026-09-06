package com.dcep.supergw.manager.dubbo.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.supergw.common.enums.GwErrorEnum;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

/**
 * @author maxinyu
 * @date 2023/11/17 9:38
 */
@Activate(group = CommonConstants.PROVIDER)
public class ProviderFlowFilter implements Filter {

    @SuppressWarnings("unchecked")
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String clientAppName = RpcContext.getContext().getAttachment("remote.application");
        EnvelopeDTO<GwDTO> dto = (EnvelopeDTO<GwDTO>) (invocation.getArguments()[0]);
        String msgTp = dto.getSoapHeader().getMsgTp();
        Entry entry = null;
        try {
            // 执行下一个Filter
            entry = SphU.entry(clientAppName + "-" + msgTp);
            Result result = invoker.invoke(invocation);
            return result;
        } catch (BlockException e) {
            throw new DcepException(GwErrorEnum.LIMITING_ERROR.getCode(), GwErrorEnum.LIMITING_ERROR.getDescription());
        } finally {
            if (null != entry) {
                entry.exit();
            }
        }
    }
}
