package com.petshop.mapper.MapperImp;

import com.petshop.mapper.Mapper;
import com.petshop.models.dto.request.UserDto;
import com.petshop.models.entities.User;
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
        return modelMapper.map(user,UserDto.class);
    }
    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto,User.class);
    }
}
