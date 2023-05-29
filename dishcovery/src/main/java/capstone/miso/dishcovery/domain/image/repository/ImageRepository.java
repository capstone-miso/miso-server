package capstone.miso.dishcovery.domain.image.repository;

import capstone.miso.dishcovery.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/

public interface ImageRepository extends JpaRepository<Image, Long> {
    boolean existsByImageUrlAndPhotoId(String imageUrl, String photoId);
}
