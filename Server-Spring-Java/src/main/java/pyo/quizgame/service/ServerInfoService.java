package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.ServerInfo;
import pyo.quizgame.respository.ServerInfoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServerInfoService {

    private final ServerInfoRepository serverInfoRepository;

    public void save(ServerInfo serverInfo)
    {
        serverInfoRepository.save(serverInfo);
    }


    public Optional<ServerInfo> getInfo()
    {
        return serverInfoRepository.findById(0L);
    }
}
