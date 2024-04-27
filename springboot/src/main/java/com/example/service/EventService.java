package com.example.service;

import com.example.entity.*;
import com.example.mapper.*;
import com.example.utils.PSO;
import com.example.utils.TokenUtils;
import com.example.utils.UserCF;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公告信息表业务处理
 **/
@Service
public class EventService {

    @Resource
    private EventMapper eventMapper;
    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RepositoryMapper repositoryMapper;
    @Resource
    private UseRecordMapper useRecordMapper;

    /**
     * 新增
     */
    public void add(Event event) {
        eventMapper.insert(event);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        eventMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            eventMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Event event) {
        eventMapper.updateById(event);
    }

    /**
     * 根据ID查询
     */
    public Event selectById(Integer id) {
        return eventMapper.selectById(id);
    }


    public List<Event> selectByUserAndDate(Integer id, Date date) {
        return eventMapper.selectByUserAndDate(id, date);
    }

    /**
     * 查询所有
     */
    public List<Event> selectAll(Event event) {
        return eventMapper.selectAll(event);
    }

    /**
     * 分页查询
     */
    public PageInfo<Event> selectPage(Event event, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Event> list = eventMapper.selectAll(event);
        return PageInfo.of(list);
    }

    public List<Event> selectTop10() {
        return eventMapper.selectTop10();
    }

    public List<Event> selectByCategoryId(Integer id) {
        return eventMapper.selectByCategoryId(id);
    }

    public List<Event> selectByBusinessId(Integer id) {return eventMapper.selectByBusinessId(id);}

    public List<Event> selectPersonal(Integer id) {return eventMapper.selectPersonal(id);}

    public void addRepository(Event event, Integer userId) {
        eventMapper.updateById(event);
    }

    public void addInSchedule(Event event, Date date, Integer userId) {
        eventMapper.insert(event);
        Schedule schedule = scheduleMapper.selectByIdAndDate(userId, date);
        if(schedule == null){
            schedule = new Schedule(date, userId);
            scheduleMapper.insert(schedule);
        }
        eventMapper.insertIntoSchedule(event, schedule);
    }

    public void deleteInSchedule(Integer id, Date date, Integer userId) {
        Schedule schedule = scheduleMapper.selectByIdAndDate(userId, date);
        UseRecord useRecord = useRecordMapper.selectByTargetIdAndScheduleId(id, schedule.getId());
        if(useRecord != null){
            useRecordMapper.delete(id, schedule.getId());
        }
        eventMapper.deleteInSchedule(id, schedule);
        eventMapper.deleteById(id);
    }

    public Event selectFromRepository(Integer eventId, Integer userId) {
        return eventMapper.selectFromRepository(eventId, userId);
    }

    public void addRepositoryOnly(Event event, Integer userId) {
        eventMapper.addRepository(event, userId);
    }

    public List<Event> recommend(Integer categoryId, int total) {
        List<Order> orders = orderMapper.selectAll(null);
        List<Comment> comments = commentMapper.selectAll(null);
        List<Repository> repositories = repositoryMapper.selectAll(null);
        List<User> users = userMapper.selectAll(null);
        List<Event> events;
        if(categoryId == null)
            events = eventMapper.selectAll(null);
        else
            events = eventMapper.selectByCategoryId(categoryId);

        List<RelateDTO> results = new ArrayList<>();
        List<Event> ret = new ArrayList<>();

        if(events.isEmpty()){
            return ret;
        }

        for(User user:users){
            Integer userId = user.getId();
            for(Event event:events){
                if(event.getStatus() == "STATIC"){
                    Integer eventId = event.getId();
                    int index = 1;
                    Optional<Comment> commentOptional = comments.stream().filter(x->x.getEventId().equals(eventId) && x.getUserId().equals(userId)).findFirst();
                    if(commentOptional.isPresent()){
                        index += 1;
                    }
                    Optional<Order> orderOptional = orders.stream().filter(x->x != null && x.getEventId() != null && x.getEventId().equals(eventId) && x.getUserId().equals(userId)).findFirst();
                    if(orderOptional.isPresent()){
                        index += 3;
                    }
                    Optional<Repository> repositoryOptional = repositories.stream().filter(x->x != null && x.getEventId() != null && x.getEventId().equals(eventId) && x.getUserId().equals(userId)).findFirst();
                    if(repositoryOptional.isPresent()){
                        index += 2;
                    }
                    RelateDTO relateDTO = new RelateDTO(userId, eventId, index);
                    results.add(relateDTO);
                }
            }
        }

        Account user = TokenUtils.getCurrentUser();
        List<Integer> eventIds = UserCF.recommend(user.getId(), results);
        List<Event> res = eventIds.stream().map(eventId->events.stream().filter(x->x.getStatus().equals("STATIC") && x.getId().equals(eventId)).findFirst().orElse(null)).limit(6).collect(Collectors.toList());

        if(res.isEmpty()){
            return getRandomEvents(total, categoryId);
        }
        if(res.size() < total){
            int num = total - res.size();
            List<Event> list = getRandomEvents(num, categoryId);
            ret.addAll(list);
            //ret.addAll(res);
        }
        return ret;
    }

    private List<Event> getRandomEvents(int num, Integer categoryId) {
        List<Event> list = new ArrayList<>(num);
        List<Event> events;
        if(categoryId == null)
            events = eventMapper.selectAllSTATIC(null);
        else
            events = eventMapper.selectByCategoryId(categoryId);
        for (int i = 0; i < num; i++) {
            int index = new Random().nextInt(events.size());
            list.add(events.get(index));
        }
        return list;
    }

    public PageInfo<Event> selectRepositoryPage(Event event, Integer repositoryUserId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Event> list = eventMapper.selectAllFromRepository(event, repositoryUserId);
        return PageInfo.of(list);
    }

    public void addInRepository(Event event, Integer userId) {
        eventMapper.insert(event);
        repositoryMapper.insert(new Repository(userId, event.getId()));
    }

    public List<Event> selectAllFromRepository(Event event, Integer repositoryUserId) {
        return eventMapper.selectAllFromRepository(event, repositoryUserId);
    }

    public void sendEvent(Event event, Integer teamId, Integer currentUserId) {
        List<User> users = userMapper.selectByTeam(teamId);
        for(User user : users){
            Schedule schedule = scheduleMapper.selectByIdAndDate(user.getId(), event.getDate());
            if(schedule == null){
                schedule = new Schedule();
                schedule.setDate(event.getDate());
                schedule.setUserId(user.getId());
                scheduleMapper.insert(schedule);
            }
            Event newEvent = new Event();
            BeanUtils.copyProperties(event, newEvent);
            if(Objects.equals(user.getId(), currentUserId)){
                newEvent.setStatus("PERSONAL");
            }else {
                newEvent.setStatus("UNCONFIRMED");
            }
            newEvent.setId(null);
            eventMapper.insert(newEvent);
            eventMapper.insertIntoSchedule(newEvent, schedule);
        }

    }

    /**
     * 计算两个日期相差的天数（忽略时、分、秒）
     *
     * @param date1 第一个日期对象
     * @param date2 第二个日期对象
     * @return 相差的天数（整数）
     */
    private static int getDifferenceInDays(Date date1, Date date2) {
        long millisecondDiff = Math.abs(date2.getTime() - date1.getTime());
        return (int) (millisecondDiff / (1000 * 60 * 60 * 24));
    }

    private static Date mergeDateAndTime(Date currentDate, double hourOfDay) {
        // 将Date类型转换为LocalDate
        LocalDate localDate = currentDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 将Double类型的时间小时转换为LocalTime
        LocalTime localTime = LocalTime.ofNanoOfDay((long) (hourOfDay * 3600_000_000_000L));

        // 合并LocalDate和LocalTime得到LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        // 将LocalDateTime转换回旧版的Date类型
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Event selectRandom(List<Event> events){
        int n = events.size();
        Random random = new Random();
        int k = random.nextInt(n);
        Event res = events.get(k);
        events.remove(k);
        return res;
    }

    public List<Event> generateSchedule(Integer userId, Date date) {
        Double delta = 0.99;

        List<Event> input = new ArrayList<>();
        List<Event> res = new ArrayList<>();

        List<UseRecord> useRecords = useRecordMapper.select7Days(userId, date);
        Map<Integer, Double> V = new HashMap<>();
        Map<Integer, Event> mp = new HashMap<>();
        for(UseRecord useRecord : useRecords){
            Event event = eventMapper.selectById(useRecord.getInitialId());
            Schedule schedule = scheduleMapper.selectById(useRecord.getScheduleId());
            event.setDate(schedule.getDate());
            if(!V.containsKey(event.getId())){
                V.put(event.getId(), 0.0);
                mp.put(event.getId(), event);
            }
            V.put(event.getId(), V.get(event.getId()) + Math.pow(delta, getDifferenceInDays(date, event.getDate())));
            input.add(event);
        }

        if(input.size() < 20){
            Integer num = 20 - input.size();
            List<Event> repoEvents = eventMapper.selectAllFromRepository(new Event(), userId);
            if(repoEvents.size() < num){
                for(Event event : repoEvents){
                    if(!mp.containsKey(event.getId())){
                        mp.put(event.getId(), event);
                        input.add(event);
                        V.put(event.getId(), 0.0);
                    }
                }
            } else {
                Event event = selectRandom(repoEvents);
                input.add(event);
            }
        }

        Map<Integer, Double> attributes = PSO.generate(input, V);
        attributes.forEach((id, s) -> {
            Event event = mp.get(id);
            event.setStart(mergeDateAndTime(date, s));
            res.add(event);
        });

        res.sort(Comparator.comparing(Event::getStart));

        return res;
    }

    public void applyInSchedule(Event event, Date date, Integer userId, Integer initialId) {
        UseRecord useRecord = new UseRecord();
        eventMapper.insert(event);
        Schedule schedule = scheduleMapper.selectByIdAndDate(userId, date);
        if(schedule == null){
            schedule = new Schedule(date, userId);
            scheduleMapper.insert(schedule);
        }
        eventMapper.insertIntoSchedule(event, schedule);
        useRecord.setInitialId(initialId);
        useRecord.setTargetId(event.getId());
        useRecord.setScheduleId(schedule.getId());
        useRecordMapper.insert(useRecord);
    }

    public List<History> getHistory() {
        return PSO.getHistory();
    }
}