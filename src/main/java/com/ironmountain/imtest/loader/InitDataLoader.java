package com.ironmountain.imtest.loader;


import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.services.UserService;
import com.ironmountain.imtest.transfer.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class InitDataLoader implements ApplicationRunner {

    private final UserService userService;


    @Override
    public void run(ApplicationArguments args) {
//        getCsvData("C:\\Users\\chapa\\Documents\\states.csv")
//                .map(record -> State.builder()
//                        .id(Long.parseLong(record.get("id")))
//                        .code(record.get("cve_entidad"))
//                        .name(record.get("nombre"))
//                        .active(record.get("estatus").equalsIgnoreCase("t"))
//                        .build())
//                .map(state -> {
//                    try {
//                        return stateRepository.save(state);
//                    } catch (BusinessException e) {
//                        System.out.println(e.getMessage());
//                        return null;
//                    }
//                }).filter(Objects::nonNull)
//                .forEach(System.out::println);

//        getCsvData("C:\\Users\\chapa\\Documents\\counties.csv")
//                .map(record -> County.builder()
//                        .id(Long.parseLong(record.get("id")))
//                        .state(State.builder().id(Long.parseLong(record.get("id_estado"))).build())
//                        .code(record.get("cve_municipio"))
//                        .name(record.get("nombre"))
//                        .active(record.get("estatus").equalsIgnoreCase("t"))
//                        .build())
//                .map(county -> {
//                    try {
//                        return countyRepository.save(county);
//                    } catch (BusinessException e) {
//                        System.out.println(e.getMessage());
//                        return null;
//                    }
//                }).filter(Objects::nonNull)
//                .forEach(System.out::println);


        Stream.of(
            UserDTO.builder()
                .username("admin")
                .password("admin123")
                .role("ADMIN")
                .build(),
            UserDTO.builder()
                .username("reader")
                .password("reader123")
                .role("READER")
                .build()
        ).map(data -> {
            try {
                return userService.createUser(data);
            } catch (BusinessException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull)
        .forEach(System.out::println);
    }
}
