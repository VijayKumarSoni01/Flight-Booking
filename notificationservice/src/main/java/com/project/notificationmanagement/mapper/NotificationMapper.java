package com.project.notificationmanagement.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.project.notificationmanagement.dto.response.EmailResponse;
import com.project.notificationmanagement.dto.response.NotificationResponse;
import com.project.notificationmanagement.entity.Notification;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {


    @Mapping(
            target = "notificationId",
            source = "id"
    )
    EmailResponse toEmailResponse(
            Notification notification
    );



    @Mapping(
            target = "notificationId",
            source = "id"
    )
    @Mapping(
            target = "message",
            source = "body"
    )
    NotificationResponse toNotificationResponse(
            Notification notification
    );



    List<EmailResponse> toEmailResponseList(
            List<Notification> notifications
    );



    List<NotificationResponse> toNotificationResponseList(
            List<Notification> notifications
    );

}