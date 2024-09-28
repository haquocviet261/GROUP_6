package com.iot.mapper.MapperImp;

import com.iot.mapper.Mapper;
import com.iot.model.dto.request.UserDTO;
import com.iot.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDTO> {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO mapTo(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User mapFrom(UserDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
