package com.nilo.dms.facade;

import com.nilo.dms.facade.model.AllocatingCenterFacade;
import com.nilo.dms.facade.model.ContractAreaFacade;
import com.nilo.dms.facade.model.DistributionNetworkFacade;
import com.nilo.dms.facade.response.FacadeResponse;

/**
 * Created by Administrator on 2017/8/17.
 */
public interface NetworkServiceFacade {

    FacadeResponse addContractArea(ContractAreaFacade merchantInfo);

    FacadeResponse addDistributionNetwork(DistributionNetworkFacade distributionNetworkFacade);

    FacadeResponse addAllocatingCenter(AllocatingCenterFacade allocatingCenterFacade);
}
