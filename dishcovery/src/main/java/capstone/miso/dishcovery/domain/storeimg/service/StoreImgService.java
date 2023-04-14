package capstone.miso.dishcovery.domain.storeimg.service;

import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.storeimg.StoreImg;
import capstone.miso.dishcovery.domain.storeimg.dto.StoreImageDto;
import capstone.miso.dishcovery.domain.storeimg.repository.StoreImgRepository;
import capstone.miso.dishcovery.dto.StoreImgDto;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/
@Service
@RequiredArgsConstructor
public class StoreImgService {

    public List<StoreImg> saveMainStoreImages(Store store) {
        // URL 객체 생성
        try {
            URL url = new URL("https://place.map.kakao.com/photolist/v/" + store.getSid());
            ObjectMapper objectMapper = new ObjectMapper();
            StoreImageDto storeImageDto = objectMapper.readValue(url, StoreImageDto.class);

            List<StoreImg> storeImgs = new ArrayList<>();

            storeImageDto.getPhotoViewer().getList().forEach(photo -> {
                StoreImg s = StoreImg.builder().imageUrl(photo.url).build();
                store.addStoreImg(s);
                storeImgs.add(s);
            });

            return storeImgs;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public StoreImgDto getStoreImgs(Store store, String moreId, String basis) {
        if (moreId == null)
            moreId = "";

        String type = basis != null ? "sorted" : "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String stringUrl = String.format("https://place.map.kakao.com/photolist/v/%d?moreid=%s",
                    store.getSid(), moreId);
            StoreImageDto storeImageDto = objectMapper.readValue(new URL(stringUrl), StoreImageDto.class);

            List<String> images = new ArrayList<>();

            storeImageDto.getPhotoViewer().getList().forEach(photo -> images.add(photo.url));

            StoreImgDto storeImgDto = new StoreImgDto(
                    storeImageDto.getPhotoViewer().placenamefull,
                    storeImageDto.getPhotoViewer().type,
                    storeImageDto.getPhotoViewer().basis,
                    storeImageDto.getPhotoViewer().moreId,
                    images
            );
            return storeImgDto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
