package com.iot.mapper.MapperImp;

import com.iot.mapper.Mapper;
import com.iot.model.dto.request.EditDTO;
import com.iot.model.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EditMapper implements Mapper<User, EditDTO> {
    private ModelMapper modelMapper;
    @Override
    public EditDTO mapTo(User user) {
        return modelMapper.map(user, EditDTO.class);
    }

    @Override
    public User mapFrom(EditDTO editDTO) {
        return modelMapper.map(editDTO,User.class);
    }
}
