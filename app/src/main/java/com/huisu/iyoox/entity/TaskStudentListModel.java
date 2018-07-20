package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/7/19
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TaskStudentListModel {
    //    {
//        "rowid": 2,
//            "id": 3,
//            "teacher_id": 16,
//            "classroom_id": 21,
//            "createdate": "2018-07-18T11:52:57.83",
//            "zhishidian_id": 23837,
//            "xueke_id": null,
//            "name": "基础拼音练习",
//            "desc": "remark",
//            "source": null,
//            "isvalid": 1,
//            "need_score": 0,
//            "start_time": "2018-07-01T00:00:00",
//            "end_time": "2018-07-18T14:40:00",
//            "work_count": 0,
//            "classroom_no": "400040",
//            "xueke_name": "英语"
//    }
    private int rowid;
    private int classroom_id;
    private int zhishidian_id;
    private String start_time;
    private String end_time;
    private String work_count;
    private String xueke_name;
    private String classroom_no;
    private String name;

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public int getZhishidian_id() {
        return zhishidian_id;
    }

    public void setZhishidian_id(int zhishidian_id) {
        this.zhishidian_id = zhishidian_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getWork_count() {
        return work_count;
    }

    public void setWork_count(String work_count) {
        this.work_count = work_count;
    }

    public String getXueke_name() {
        return xueke_name;
    }

    public void setXueke_name(String xueke_name) {
        this.xueke_name = xueke_name;
    }

    public String getClassroom_no() {
        return classroom_no;
    }

    public void setClassroom_no(String classroom_no) {
        this.classroom_no = classroom_no;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
