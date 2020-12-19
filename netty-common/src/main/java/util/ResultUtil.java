package util;

import entity.Result;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: Created in 15:01 2019/3/26
 */
public class ResultUtil {

    public static Result<Object> success(){
        return new Result<>(true,"成功",null);
    }

    public static Result<Object> success(Object o){
        return new Result<>(true,"成功",o);
    }

    public static Result<Object> error(){
        return new Result<>(false,"失败",null);
    }

    public static Result error(Object o){
        return new Result<>(false,"失败",o);
    }
}
