package com.football.core.service.param;

import com.football.common.constant.Constant;
import com.football.common.exception.CommonException;
import com.football.common.message.MessageCommon;
import com.football.common.model.param.Param;
import com.football.common.model.param.ParamKey;
import com.football.common.response.Response;
import com.football.common.util.Resource;
import com.football.common.util.StringCommon;
import com.football.core.repository.ParamRepository;
import com.football.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ParamServiceImpl extends BaseService implements ParamService {
    @Autowired
    ParamRepository paramRepository;

    @Autowired
    DataSource dataSource;

    @Override
    public Param create(Param param) throws Exception {
        if (param == null
                || param.getParamKey() == null
                || StringCommon.isNullOrBlank(param.getParamKey().getType())
                || StringCommon.isNullOrBlank(param.getParamKey().getCode())
                || StringCommon.isNullOrBlank(param.getValue())
                )
            throw new CommonException(Response.BAD_REQUEST, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.IS_NULL), Constant.TABLE.PARAM));
        else {
            //Check param exist
            Param oldParam = findById(param.getParamKey().getType(), param.getParamKey().getCode());
            if (oldParam != null)
                throw new CommonException(Response.OBJECT_IS_EXIST, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.IS_EXISTS), Constant.TABLE.PARAM));
            else
                return paramRepository.save(param);
        }
    }

    @Override
    public Param findById(String type, String code) throws Exception {
        return paramRepository.findOne(new ParamKey(type, code));
    }

    @Override
    public List<Param> findByStatus(int status) throws Exception {
        return paramRepository.findByStatus(status);
    }

    @Override
    public Iterable<Param> findAll() throws Exception {
        return paramRepository.findAll();
    }

    @Override
    public Param update(Param param) throws Exception {
        if (param == null
                || param.getParamKey() == null
                || StringCommon.isNullOrBlank(param.getParamKey().getType())
                || StringCommon.isNullOrBlank(param.getParamKey().getCode())
                )
            throw new CommonException(Response.BAD_REQUEST, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.IS_NULL), Constant.TABLE.PARAM));
        else {
            Param paramOld = paramRepository.findOne(param.getParamKey());
            if (paramOld == null)
                throw new CommonException(Response.NOT_FOUND, MessageCommon.getMessage(Resource.getMessageResoudrce(Constant.RESOURCE.KEY.NOT_FOUND), Constant.TABLE.PARAM));
            else {
                paramOld.setValue(!StringCommon.isNullOrBlank(param.getValue()) ? param.getValue() : paramOld.getValue());
                paramOld.setName(!StringCommon.isNullOrBlank(param.getName()) ? param.getName() : paramOld.getName());
                paramOld.setStatus(param.getStatus());
                return paramRepository.save(paramOld);
            }
        }
    }
}
