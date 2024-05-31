package io.github.patternknife.pxbsample.domain.customer.dto;

import io.github.patternknife.pxbsample.domain.customer.entity.Customer;
import io.github.patternknife.pxbsample.domain.customer.entity.Password;
import io.github.patternknife.pxbsample.util.CustomUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class CustomerReqDTO {

    @Getter
    public static class CreateSocialNew {
        @NotBlank
        private String idName;
    }

    @Getter
    public static class Create {
        public String appToken;

        @NotBlank
        public String name;

        @NotNull
        @Past
        private LocalDate birthday;

        @NotBlank
        private String sex;

        @NotBlank
        private String hp;


        private String idName;


        @NotBlank
        private String password;
        private String email;

        @NotBlank
        private String ci;

        public Customer toEntity() {
            Customer.CustomerBuilder builder = Customer.builder()
                    .name(this.name)
                    .birthday(this.birthday)
                    .sex(this.sex)
                    .hp(CustomUtils.removeSpecialCharacters(this.hp))
                    .idName(this.idName)
                    .password(Password.builder().value(this.password).build())
                    .email(this.email);
            return builder.build();
        }
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Update {

        @NotBlank
        private String idName;
        @NotBlank
        public String name;
        @NotBlank
        public String hp;

        public String email;

    }

    @Getter
    public static class UpdatePasswordAndEmail {
        @NotBlank
        private String password;
        public String email;
    }

}

