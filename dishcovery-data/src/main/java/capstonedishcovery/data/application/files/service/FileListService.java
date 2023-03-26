package capstonedishcovery.data.application.files.service;

import capstonedishcovery.data.application.files.Files;
import capstonedishcovery.data.application.files.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/
@Service
@RequiredArgsConstructor
public class FileListService {
    private final FileRepository fileRepository;
    public Files saveDownloadedFileInfo(String fileName, String url){
        Files file = Files.builder()
                .title(fileName)
                .fileUrl(url)
                .converted(false)
                .build();
        return fileRepository.save(file);
    }
    public void changeFileConvertedStatus(Long fid){
        Optional<Files> result = fileRepository.findById(fid);
        Files file = result.orElseThrow();
        file.changeConvertStatus(true);

        fileRepository.save(file);
    }
    public List<Files> getFileLists(){
        List<Files> result = fileRepository.findAll();
        return result;
    }
}
