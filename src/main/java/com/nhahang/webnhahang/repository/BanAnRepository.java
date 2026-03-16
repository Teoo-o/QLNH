package com.nhahang.webnhahang.repository;

import com.nhahang.webnhahang.entity.BanAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BanAnRepository extends JpaRepository<BanAn, String> {

    // Tự động sinh ra lệnh SQL: SELECT * FROM BAN_AN WHERE Status = 'Trống'
    @Query("SELECT b FROM BanAn b WHERE b.status = 'Trống'")
    List<BanAn> timCacBanDangTrong();
}