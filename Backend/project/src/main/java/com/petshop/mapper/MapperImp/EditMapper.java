package com.petshop.mapper.MapperImp;

import com.petshop.mapper.Mapper;
import com.petshop.models.dto.request.EditDTO;
import com.petshop.models.entities.User;
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
