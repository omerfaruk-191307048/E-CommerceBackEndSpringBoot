package com.securityVideoProject.security.business;

import com.securityVideoProject.security.auth.enums.EntityType;
import com.securityVideoProject.security.core.utilities.results.DataResult;
import com.securityVideoProject.security.core.utilities.results.ErrorDataResult;
import com.securityVideoProject.security.core.utilities.results.SuccessDataResult;
import com.securityVideoProject.security.dataAccess.abstracts.UserRepository;
import com.securityVideoProject.security.dto.response.UserResponseDto;
import com.securityVideoProject.security.entities.user.User;
import com.securityVideoProject.security.dto.request.UserRequestDto;
import com.securityVideoProject.security.exceptions.NotFoundException;
import com.securityVideoProject.security.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public DataResult<List<UserResponseDto>> findUserByUserNameContains(String name) {
        if (userRepository.findUserByFirstnameContains(name).isEmpty())
            throw new NotFoundException(EntityType.User + "s not found");
        return new SuccessDataResult<List<UserResponseDto>>
                (userMapper.toDtoList(userRepository.findUserByFirstnameContains(name))
                        , "Users successfully found");
    }


    public DataResult<List<UserResponseDto>> findUserByName(String userName) {
        if (userRepository.findUserByFirstname(userName).isEmpty())
            throw new NotFoundException(EntityType.User + "s not found");
        return new SuccessDataResult<List<UserResponseDto>>
                (userMapper.toDtoList(userRepository.findUserByFirstname(userName))
                        , "Users successfully found");
    }


    public DataResult<List<UserResponseDto>> findAllUser() {
        if (userRepository.findAll().isEmpty())
            throw new NotFoundException(EntityType.User + "s not found");
        return new SuccessDataResult<List<UserResponseDto>>
                (userMapper.toDtoList(userRepository.findAll())
                        ,  "Users successfully found");
    }

    public DataResult<UserResponseDto> findUserById(Integer userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException(EntityType.User + " not found");
        return new SuccessDataResult<UserResponseDto>
                (userMapper.toDto(userRepository.findById(userId).get())
                        , "User successfully found");
    }


    public DataResult<List<UserResponseDto>> findUserByCreatedAt
            (String startDateInput, String endDateInput) throws ParseException {
        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateInput);
        Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateInput);
        if (containsLetters(startDateInput) == true ||
                containsLetters(endDateInput) == true) {
            return new ErrorDataResult("You can not use letter when setting the date!");
        } else if (userRepository.findByCreatedAtBetween(startDate, endDate).isEmpty())
            throw new NotFoundException(EntityType.User + "s not found between dates");
        return new SuccessDataResult<List<UserResponseDto>>
                (userMapper.toDtoList(userRepository.findByCreatedAtBetween(startDate, endDate))
                        , "Users successfully found between dates");

    }

    public static boolean containsLetters(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        for (int i = 0; i < string.length(); ++i) {
            if (Character.isLetter(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public DataResult<UserResponseDto> updateUser(Integer userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId).get();
        if (userRepository.findById(user.getId()).isEmpty())
            throw new NotFoundException(EntityType.User + " not found");
        return new SuccessDataResult<UserResponseDto>
                (userMapper.toDto(userRepository.save(userMapper.update(userRequestDto, user)))
                        , "User successfully updated");
    }

    public DataResult<UserResponseDto> deleteUserById(Integer id) {
        User user = userRepository.findById(id).get();
        if (userRepository.findById(user.getId()).isEmpty())
            throw new NotFoundException(EntityType.User + " not found");
        userRepository.deleteById(id);
        return new SuccessDataResult<UserResponseDto>("User successfully deleted");
    }

}
