package hyman.sr.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hyman.sr.mapper.UserMapper;
import hyman.sr.model.User;
import hyman.sr.service.UserService;

/**
 * userService
 * 
 * 缓存机制说明：所有的查询结果都放进了缓存，也就是把MySQL查询的结果放到了redis中去，
 * 然后第二次发起该条查询时就可以从redis中去读取查询的结果，从而不与MySQL交互，从而达到优化的效果，
 * redis的查询速度之于MySQL的查询速度相当于 内存读写速度 /硬盘读写速度 
 * @Cacheable("a")注解的意义就是把该方法的查询结果放到redis中去，下一次再发起查询就去redis中去取，存在redis中的数据的key就是a；
 * @CacheEvict(value={"a","b"},allEntries=true) 的意思就是执行该方法后要清除redis中key名称为a,b的数据；
 */
@Service("userService")
@Transactional(rollbackFor=Exception.class)  
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper iUserDao;

    /**
     * 从配置文件中读取env_name的值，不同环境配置了不同的配置文件，通过web.xml中 
     * <param-name>spring.profiles.active</param-name>来指定
     * 每个环境对应的配置文件配置在applicationContext-profile.xml中
     */
    @Value("${env_name}")
    private String env_name;
    
    @Cacheable("getAllUser")
    @Override
    public List<User> getAllUser() {
    	System.out.println("env_name is : "+env_name);
        return this.iUserDao.selectAllUser();
    }

    @CacheEvict(value= {"getAllUser","getUserById","findUsers"},allEntries=true)//清空缓存，allEntries变量表示所有对象的缓存都清除
    @Override
    public void insertUser(User user) {
        this.iUserDao.insertUser(user);
    }

    @CacheEvict(value= {"getAllUser","getUserById","findUsers"},allEntries=true)
    @Override
    public void deleteUser(int id) {
        this.iUserDao.deleteUser(id);
    }

    @Cacheable("findUsers")
    @Override
    public List<User> findUsers(String keyWords) {
        return iUserDao.findUsers(keyWords);
    }

    @CacheEvict(value= {"getAllUser","getUserById","findUsers"},allEntries=true)
    @Override
    public void editUser(User user) {
        this.iUserDao.editUser(user);
    }

	@Override
	public User getUserById(int userId) {
		return null;
	}
}
