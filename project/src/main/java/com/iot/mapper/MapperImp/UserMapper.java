package com.iot.mapper.MapperImp;

import com.iot.mapper.Mapper;
import com.iot.model.dto.request.UserDto;
import com.iot.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDto> {
    private ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(User user) {
        return modelMapper.map(user, UserDto.class);
    }
    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto,User.class);
    }
}
