//package com.LIB.MessagingSystem.Service;
//
//import com.LIB.MessagingSystem.Model.FilePrivilege;
//import com.LIB.MessagingSystem.Repository.FilePrivilegeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import java.util.Optional;
//
//
//@Service
//@RequiredArgsConstructor
//public class FilePrivilegeService {
//
//    private final FilePrivilegeRepository filePrivilegeRepository;
//    FilePrivilege filePrivilege = new FilePrivilege();
//
//    public boolean canUserViewFile(String userId, String attachmentId) {
//        Optional<FilePrivilege> privilege = filePrivilegeRepository.findByAttachmentIdAndUserId(attachmentId, userId);
//        return privilege.isPresent() && privilege.get().isCanView();
//    }
//
//    public boolean canUserDownloadFile(String userId, String attachmentId) {
//        Optional<FilePrivilege> privilege = filePrivilegeRepository.findByAttachmentIdAndUserId(attachmentId, userId);
//        return privilege.isPresent() && privilege.get().isCanDownload();
//    } return filePrivilege.() && filePrivilege.;
//    }


