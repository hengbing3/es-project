package com.christer.project.job.cycle;

import com.christer.project.esmapper.PostEsMapper;
import com.christer.project.mapper.PostMapper;
import com.christer.project.model.dto.post.PostEsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christer
 * @version 1.0
 * @date 2024-01-15 22:43
 * Description:
 */
//@Component
@Slf4j
public class IncSyncPostToEs {

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostEsMapper postEsDao;



    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // TODO 查询近 5 分钟内的数据,同步数据库到es

    }
}

