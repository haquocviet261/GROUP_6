package com.iot.mapper.MapperImp;

import com.iot.mapper.Mapper;
import com.iot.model.dto.request.EditUserDTO;
import com.iot.model.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EditMapper implements Mapper<User, EditUserDTO> {
    private final ModelMapper modelMapper;

    public EditMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EditUserDTO mapTo(User user) {
        return modelMapper.map(user, EditUserDTO.class);
    }

    @Override
    public User mapFrom(EditUserDTO editUserDTO) {
        return modelMapper.map(editUserDTO, User.class);
    }
}
