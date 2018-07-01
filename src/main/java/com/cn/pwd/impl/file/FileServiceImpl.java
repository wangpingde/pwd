package com.cn.pwd.impl.file;

import com.cn.pwd.service.IFileService;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;


import java.io.InputStream;

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;


    @Override
    public String saveFile(InputStream inputStream, String fileName, String content) {
        GridFSFile gridFSFile = gridFsTemplate.store(inputStream,fileName,content);
        if(gridFSFile!=null){
            return gridFSFile.getId().toString();
        }
        return null;
    }

    @Override
    public GridFSDBFile queryFileById(String fileId) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
    }

    @Override
    public GridFSDBFile queryFileWithQuery(Query query) {
        return gridFsTemplate.findOne(query);
    }

    @Override
    public void deleteFile(String fileId) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
    }
}
