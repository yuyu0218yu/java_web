package ${basePackage}.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${basePackage}.entity.${className};
import ${basePackage}.mapper.${className}Mapper;
import ${basePackage}.service.${className}Service;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * ${className} Service实现类
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Service
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements ${className}Service {

}
