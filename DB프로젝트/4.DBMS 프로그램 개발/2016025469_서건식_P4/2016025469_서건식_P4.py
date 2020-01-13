import pymysql as pms
from datetime import date

connection = pms.connect(host = 'localhost',
                         port=3306,
                         user='root',
                         password='ad',
                         db='music',
                         charset='utf8')

cursor = connection.cursor()

today = date.today()

def print_list(sql):
    cursor.execute(sql)
    result=cursor.fetchall()
    for i in range(len(result)):
        print(i,":",end = ' ')
        
        for j in range(len(result[i])):
            print(result[i][j],end = ' | ')


        print('\n')
        
        
    return 0


def get_attribute(sql):
    cursor.execute(sql)
    result=cursor.fetchone()
    return result[0]


def insert_into(sql):
    cursor.execute(sql)
    connection.commit()
    return 0


def check_exist(sql):
    cursor.execute(sql)
    success = cursor.fetchone()
    return success[0]


def delete_from(sql):
    cursor.execute(sql)
    connection.commit()

def print_playlist(sql):
    print("\n플레이리스트에 담긴 음악 목록:")
    print("----------------------------------------------------------------------------------------------------------------------------------")
    print("정보 : 제목  /  아티스트  /  장르  / 플레이시간 / 발매일자 / 앨범제목 /  뮤직비디오  /  순위")
    print("----------------------------------------------------------------------------------------------------------------------------------")
    print_list(sql)
    print("----------------------------------------------------------------------------------------------------------------------------------")
    
while 1:
    print("""----------------------------------------------------------------------------------------------------------------------------------
0. 종료\n1. 사용자 로그인\n2. 관리자 로그인\n3. 사용자 회원가입\n4. 사용자 회원탈퇴\n5. 관리자 등록
----------------------------------------------------------------------------------------------------------------------------------\n""")
    num1 = input("Input: ")
    
    
    if num1 == '0':
        break
    elif num1 == '1':
        
        Id = input("아이디: ")
        Pw = input("비밀번호: ")
        
        sql = "select EXISTS (select * from 사용자 where ID = '{}' and Password = '{}') as success".format(Id,Pw)
        
        if check_exist(sql):
            sql = "select 소유한이용권 from 사용자 where ID = '{}'".format(Id)
            ispass =get_attribute(sql)
      
    
            while 1:
                sql = "select 소유한이용권 from 사용자 where ID = '{}'".format(Id)
                ispass =get_attribute(sql)
                print('\n\n\n----------------------------------------------------------------------------------------------------------------------------------\n플레이리스트 목록:\n ')
                sql = "select distinct `제목` from 플레이리스트 where `소유한사용자` = '{}'".format(Id)
                cursor.execute(sql)
                result=cursor.fetchall()
                for i in range(len(result)):
                    print(i,":",end = ' ')
                
                    for j in range(len(result[i])):
                        print(result[i][j],end = ' ')


                    print('\n')
                
                
                print("""----------------------------------------------------------------------------------------------------------------------------------
0. 이전화면으로\n1. 전체 음악 순위 보기\n2. 플레이리스트에 음악 넣기\n3. 플레이리스트에서 음악 삭제\n4. 플레이리스트 열기\n5. 플레이리스트 공유하기\n6. 공유받은 플레이리스트 보기\n7. 이용권 구매하기\n8. 구매한 이용권 보기
----------------------------------------------------------------------------------------------------------------------------------\n""")
                num2 = input("Input: ")

                if num2 == '0':
                    break

                elif num2 == '1':
                    print("----------------------------------------------------------------------------------------------------------------------------------")
                    print("정보 : 순위  /  제목  /  아티스트  /  장르  / 플레이시간 / 발매일자 / 앨범제목 /  뮤직비디오")
                    print("----------------------------------------------------------------------------------------------------------------------------------")
                    sql = "select ROW_NUMBER() OVER(ORDER BY 음악.음악ID ASC) AS 순위,제목,아티스트,장르,플레이시간,발매일자,앨범제목,뮤직비디오 from `음악`"
                    print_list(sql)
                    print("----------------------------------------------------------------------------------------------------------------------------------")

                elif num2 == '2':
                    if ispass == '정기권' or ispass == '스트리밍':
                        print("이용권을 소유하고있습니다.")
                        while 1: 
                            name = input("입력을 취소하시려면 exit을 눌러주세요.\n플레이리스트 이름을 입력하세요: ")
                            if(name == 'exit'):
                                break
                            
                            else:
                                print("\n전체 음악 목록:")
                                print("----------------------------------------------------------------------------------------------------------------------------------")
                                
                                print("정보 : 제목  /  아티스트  /  장르  / 플레이시간 / 발매일자 / 앨범제목 /  뮤직비디오  /  순위")
                                print("----------------------------------------------------------------------------------------------------------------------------------")
                                sql = "select 제목,아티스트,장르,플레이시간,발매일자,앨범제목,뮤직비디오,ROW_NUMBER() OVER(ORDER BY 음악.음악ID ASC) AS 순위 from `음악`"
                                print_list(sql)
                                print("----------------------------------------------------------------------------------------------------------------------------------\n\n")
                                sql = "select 음악.제목,아티스트,장르,플레이시간,발매일자,앨범제목,뮤직비디오,ROW_NUMBER() OVER(ORDER BY 음악.음악ID ASC) AS 순위 from `플레이리스트`,`음악`,사용자 where 플레이리스트.소유한사용자 = '{}' and 플레이리스트.소유한사용자 = 사용자.ID and `플레이리스트`.음악ID = `음악`.음악ID and `플레이리스트`.제목 = '{}'".format(Id,name)
                                print_playlist(sql)
                                while 1:
                                    print("입력을 취소하시려면 exit을 눌러주세요.")
                                    music_name = input("넣을 음악의 제목을 선택하세요: ")  
                                    sql = "select EXISTS (select * from 음악 where 제목 = '{}') as success".format(music_name)

                                    if music_name == 'exit':
                                        break
                                    elif (check_exist(sql) != 1):
                                        print("음악이 존재하지 않습니다.")
                                        print("----------------------------------------------------------------------------------------------------------------------------------")
                                    else:
                                        sql = "select `음악ID` from `음악` where `제목` = '{}'".format(music_name)
                                        music_id = get_attribute(sql)
                                        sql = "select EXISTS (select * from 플레이리스트 where 소유한사용자 = '{}' and 음악ID = {} and 제목 = '{}') as success".format(Id,music_id,name)
                                        if(check_exist(sql)):
                                            print("음악이 이미 존재합니다.")
                                            print("----------------------------------------------------------------------------------------------------------------------------------")
                                        else:
                                            sql = "insert into `플레이리스트` values ('{}',{},'{}')".format(Id,music_id,name)
                                            insert_into(sql)
                                            print("입력을 완료했습니다.")
                                            break
                           
                    else:
                        print("이용권을 구매하세요.")

                                       
                elif num2 == '3':
                    if ispass == '정기권' or ispass == '스트리밍':
                        print("이용권을 소유하고있습니다.")
                        while 1 :
                            name = input("입력을 취소하시려면 exit을 눌러주세요.\n플레이리스트 이름을 입력하세요: ")
                            sql = "select EXISTS (select * from 플레이리스트 where 제목 = '{}' and 소유한사용자 = '{}') as success".format(name,Id)
                            if name == 'exit':
                                break
                            elif(check_exist(sql)):
                                    while 1:
                                        done = 0
                                        sql = "select 음악.제목,아티스트,장르,플레이시간,발매일자,앨범제목,뮤직비디오,ROW_NUMBER() OVER(ORDER BY 음악.음악ID ASC) AS 순위 from `플레이리스트`,`음악`,사용자 where 플레이리스트.소유한사용자 = '{}' and 플레이리스트.소유한사용자 = 사용자.ID and `플레이리스트`.음악ID = `음악`.음악ID and `플레이리스트`.제목 = '{}'".format(Id,name)
                                        print_playlist(sql)
                                        print("입력을 취소하시려면 exit을 눌러주세요.")
                                        music_name = input("삭제할 음악의 제목을 선택하세요: ")
                                        
                                        sql = "select EXISTS (select * from 음악 where 제목 = '{}') as success".format(music_name)
                                        while 1 :
                                            if music_name == 'exit':
                                                done = 1
                                                break
                                            elif (check_exist(sql) != 1):
                                                print("음악이 존재하지 않습니다.\n")
                                                
                                                break
                                            else:
                                                sql = "select `음악ID` from `음악` where `제목` = '{}'".format(music_name)
                                                music_id = get_attribute(sql)
                                                sql = "select EXISTS (select * from 플레이리스트 where 소유한사용자 = '{}' and 음악ID = {} and 제목 = '{}') as success".format(Id,music_id,name)
                                                if(check_exist(sql)):
                                                    sql = "delete from `플레이리스트` where 소유한사용자 = '{}' and 음악ID = {} and 제목 = '{}'".format(Id,music_id,name)
                                                    delete_from(sql)
                                                    print("음악을 플레이리스트에서 삭제하였습니다.")
                                                    print("----------------------------------------------------------------------------------------------------------------------------------")
                                                    done = 1
                                                    break
                                        if done == 1:
                                               break
                                    done = 1

                            
                            else:
                                print("존재하지 않는 플레이리스트 입니다.")
                                print("----------------------------------------------------------------------------------------------------------------------------------")
                    else:
                        print("이용권을 구매하세요.")                

                            
                            
                elif num2 == '4':
                    if ispass == '정기권' or ispass == '스트리밍':
                        print("이용권을 소유하고있습니다.")
                        while 1:
                            name = input("입력을 취소하시려면 exit을 눌러주세요.\n플레이리스트 이름을 입력하세요: ")
                            sql = "select EXISTS (select * from 플레이리스트 where 제목 = '{}' and 소유한사용자 = '{}') as success".format(name,Id)
                            if name =='exit':
                                break
                            
                            elif(check_exist(sql)):
                                sql = "select 음악.제목,아티스트,장르,플레이시간,발매일자,앨범제목,뮤직비디오,ROW_NUMBER() OVER(ORDER BY 음악.음악ID ASC) AS 순위 from `플레이리스트`,`음악`,사용자 where 플레이리스트.소유한사용자 = '{}' and 플레이리스트.소유한사용자 = 사용자.ID and `플레이리스트`.음악ID = `음악`.음악ID and `플레이리스트`.제목 = '{}'".format(Id,name)
                                print_playlist(sql)
                                break
                            else:
                                print("잘못된 값 입니다.")
                    else:
                        print("이용권을 구매하세요.")                

                elif num2 == '5':
                    if ispass == '정기권' or ispass == '스트리밍':
                        print("이용권을 소유하고있습니다.")
                        while 1:
                            share_id = input("공유할 사용자의 ID를 입력하세요: ")
                            sql = "select EXISTS (select * from 사용자 where ID = '{}') as success".format(share_id)
                            if share_id =='exit':
                                break
                            elif(check_exist(sql)):
                                sql = "update 사용자 set 공유받은ID = '{}' where ID = '{}'".format(Id,share_id)
                                insert_into(sql)
                                print("공유를 완료하였습니다.")
                                break
                            else:
                                print("존재하지 않는 ID입니다.")
                    else:
                        print("이용권을 구매하세요.")
                        
                elif num2 == '6':
                    if ispass == '정기권' or ispass == '스트리밍':
                         print("이용권을 소유하고있습니다.")
                         while 1:
                             sql = "select 공유받은ID from 사용자 where ID = '{}'".format(Id)
                             share_id = get_attribute(sql)
                             print('\n\n\n----------------------------------------------------------------------------------------------------------------------------------\n{}의 플레이리스트 목록:\n '.format(share_id))
                             sql = "select distinct `제목` from 사용자,플레이리스트 where `소유한사용자` = 사용자.공유받은ID and 소유한사용자 = '{}'".format(share_id)
                             print_list(sql)

                             while 1: 
                                name = input("입력을 취소하시려면 exit을 눌러주세요.\n플레이리스트 이름을 입력하세요: ")
                                sql = "select EXISTS (select * from 플레이리스트 where 제목 = '{}' and 소유한사용자 = '{}') as success".format(name,share_id)
                                if name =='exit':
                                    break
                                
                                elif(check_exist(sql)):
                                    sql = "select 음악.제목,아티스트,장르,플레이시간,발매일자,앨범제목,뮤직비디오,ROW_NUMBER() OVER(ORDER BY 음악.음악ID ASC) AS 순위 from `플레이리스트`,`음악`,사용자 where 플레이리스트.소유한사용자 = '{}' and 플레이리스트.소유한사용자 = 사용자.ID and `플레이리스트`.음악ID = `음악`.음악ID and `플레이리스트`.제목 = '{}'".format(share_id,name)
                                    print_playlist(sql)
                                    break
                                else:
                                    print("잘못된 값 입니다.")
                             break
                    else:
                        print("이용권을 구매하세요.")           
                elif num2 == '7':
                     while 1:
                         sql = "select 이용권이름,기간,허용서비스,가격 from 이용권"
                         print('\n\n\n이용권 목록:\n----------------------------------------------------------------------------------------------------------------------------------')
                         print("정보 : 이용권이름  /  기간  /  허용서비스  / 가격  ")
                         print('---------------------------------------------------------------------------------------------------------------------------------')
                         print_list(sql)
                         print('---------------------------------------------------------------------------------------------------------------------------------')
                         while 1: 
                            name = input("입력을 취소하시려면 exit을 눌러주세요.\n이용권 이름을 입력하세요: ")
                            sql = "select EXISTS (select * from 이용권 where 이용권이름 = '{}') as success".format(name)
                            if name =='exit':
                                break
                            
                            elif(check_exist(sql)):
                                sql = "update 사용자 set 소유한이용권 = '{}' where ID = '{}'".format(name,Id)
                                insert_into(sql)
                                print("구매를 완료했습니다.")
                                break
                            else:
                                print("잘못된 값 입니다.")
                         break
                elif num2 == '8':
                    sql = "select 소유한이용권 from 사용자 where ID = '{}'".format(Id)
                    name = get_attribute(sql)
                    sql = "select 이용권이름,기간,허용서비스,가격 from 이용권 where 이용권.이용권이름 = '{}'".format(name)
                    print('\n\n\n이용권 목록:\n----------------------------------------------------------------------------------------------------------------------------------')
                    print("정보 : 이용권이름  /  기간  /  허용서비스  / 가격  ")
                    print('---------------------------------------------------------------------------------------------------------------------------------')
                    print_list(sql)
                    print('---------------------------------------------------------------------------------------------------------------------------------')
                else:
                    print("잘못된 입력입니다.")
                        
        else:
            print("존재하지 않는 아이디와 비밀번호 입니다. \n")
                    

        
            
       
    elif num1 == '2':
        Id = input("관리자 식별 ID를 입력하세요: ")
        sql = "select exists (select * from 관리자 where 식별_ID='{}') as success".format(Id)
        if(check_exist(sql)):
            while 1:
                print("""\n\n\n----------------------------------------------------------------------------------------------------------------------------------
0. 이전화면으로\n1. 음악 추가하기\n2. 음악 삭제하기\n3. 이용권 추가하기\n4. 이용권 삭제하기\n5. 부서 추가하기\n6. 부서 정보 출력\n7. 사원 정보 출력
----------------------------------------------------------------------------------------------------------------------------------\n""")
                num2 = input("Input: ")

                if num2 == '0':
                    break
                elif num2 == '1':
                    while 1:
                        name = input("입력을 취소하시려면 exit을 입력하세요.\n추가할 음악의 정보를 입력합니다.\n제목: ")
                        if name == 'exit':
                            break
                        else:
                            while 1:
                                music_id = input("입력을 취소하시려면 exit을 입력하세요.\n음악 ID: ")
                                sql = "select exists (select * from 음악 where 음악ID='{}') as success".format(music_id)
                                if music_id == 'exit':
                                    break
                                elif(check_exist(sql)):
                                    print("존재하는 음악 ID 입니다. 다시 입력하세요.")
                            
                                else:
                                    while 1:
                                        artist = input("아티스트 이름: ")
                                        genre= input("장르: ")
                                        lyrics = input("가사: ")
                                        play_time = input("플레이 시간 양식: m분ss초\n플레이시간: ")
                                        enroll_date = today.isoformat()
                                        release_date = input("발매일자 양식 : yyyy-mm-dd\n발매일자: ")
                                        album_name = input("앨범 제목: ")
                                        mv=input("뮤직비디오 링크: ")
                                        sql = "insert into 음악 values ('{}','{}','{}','{}','{}','{}','{}','{}','{}','{}')".format(name,artist,genre,lyrics,play_time,enroll_date,release_date,album_name,mv,music_id)
                                        insert_into(sql)
                                        print("입력에 성공하였습니다.")
                                        print("----------------------------------------------------------------------------------------------------------------------------------\n\n\n")
                                        break

                                    break   
                elif num2 == '2':
                    while 1:
                        print("\n\n\n전체 음악 목록:")
                        print("----------------------------------------------------------------------------------------------------------------------------------")  
                        print("정보 : 제목  /  음악ID")
                        print("----------------------------------------------------------------------------------------------------------------------------------\n")
                        
                        sql = "select 제목,음악ID from `음악`"
                        print_list(sql)
                        print("----------------------------------------------------------------------------------------------------------------------------------")
                        
                        music_id=input("입력을 취소하시려면 exit을 눌러주세요.\n삭제할 음악의 음악 ID를 입력하세요: ")
                        sql = "select EXISTS (select * from 음악 where 음악ID = '{}') as success".format(music_id)
                        if music_id == 'exit':
                            break
                        elif(check_exist(sql)):
                            sql = "delete from 플레이리스트 where 음악ID = {}".format(music_id)
                            delete_from(sql)
                            sql = "delete from `음악` where 음악ID = {}".format(music_id)
                            delete_from(sql)
                            print("음악을 삭제하였습니다.")
                        else:
                            print("존재하지 않는 음악 ID입니다.")
                elif num2 == '3':
                    while 1:
                        name = input("입력을 취소하시려면 exit을 입력하세요.\n이용권 이름을 입력하세요:")
                        sql = "select EXISTS (select * from 이용권 where 이용권이름 = '{}') as success".format(name)
                       
                        if name == 'exit':
                            break
                        elif(check_exist(sql)):
                            print("이미 존재하는 이용권 이름 입니다.")
                        else:
                            event = input("이벤트 유무를 0또는 1로 입력하세요: ")
                            period = input("기간을 입력하세요. ex: 2달이면 2m, 1주면 1w입력: ")
                            service = input("허용서비스를 입력하세요. \n(streaming, download , all) 중 하나: ")
                            price = input("가격을 숫자만 입력하세요. 단위: won: ")

                            sql = "insert into 이용권 values('{}','{}','{}','{}','{}')".format(name,event,period,service,price)
                            insert_into(sql)
                            print("입력을 완료하였습니다.")
                            break
                
                elif num2 == '4':
                    while 1:
                        name = input("입력을 취소하시려면 exit을 입력하세요.\n이용권 이름을 입력하세요:")
                        sql = "select EXISTS (select * from 이용권 where 이용권이름 = '{}') as success".format(name)
                       
                        if name == 'exit':
                            break
                        elif(check_exist(sql)):
                            
                            sql = "delete from 이용권 where 이용권이름 = '{}'".format(name)
                            delete_from(sql)
                            print("삭제를 완료하였습니다.")
                            break
                            
                        else:
                            print("존재하지 않는 이용권 이름 입니다.")
                
                elif num2 == '5':
                    while 1:
                        Dno = input("등록을 취소하시려면 exit을 입력하세요.\n부서 번호를 입력하세요: ")
                        if(Dno == 'exit'):
                            break
                        sql = "select EXISTS (select * from 부서 where 식별번호 = '{}') as success".format(Dno)
                       

                        if(check_exist(sql)):
                            print("이미 존재하는 부서 번호 입니다.\n")
            
                        else:
                            ssn = input("부장으로 임명할 사원의 관리자ID를 입력하세요: ")
                            sql = "select EXISTS (select * from 관리자 where 식별_ID = '{}') as success".format(ssn)
                            if(check_exist(sql)):
                                name = input("부서 이름을 입력하세요: ")
                                
                                location = []
                                num = 0
                                
                                while 1:
                                    temp = input("\n입력을 그만하려면 exit을 입력하세요. 최대 10개까지 입력 가능합니다.\n부서의 위치를 입력하세요: ")
                                    if temp == 'exit':
                                        break
                                    elif num == 9:
                                        break
                                    else:
                                        location.append(temp)
                                        num = num+1
                                        
                                area = []
                                num = 0
                                while 1:
                                    temp = input("\n입력을 그만하려면 exit을 입력하세요. 최대 10개까지 입력 가능합니다.\n관리 영역을 입력하세요: ")
                                    if temp == 'exit':
                                        break
                                    elif num == 9:
                                        break
                                    else:
                                        area.append(temp)
                                        num = num+1
                                sql ="insert into 부서 values ('{}','{}','{}')".format(Dno,name,ssn)
                                insert_into(sql)
                                for i in range(len(location)):
                                    sql = "insert into 부서위치 values ('{}','{}')".format(Dno,location[i])
                                    insert_into(sql)
                                for i in range(len(area)):
                                    sql = "insert into 관리영역 values ('{}','{}')".format(Dno,area[i])
                                    insert_into(sql)
                                print("입력을 완료하였습니다.")
                                break
                            else:
                                print("존재하지 않는 관리자입니다.\n")

                            
                        
                elif num2 == '6':
                    print("정보 : 식별번호 /  부서이름 /  부장이름  /  부서위치  /  관리영역 ")
                    print("----------------------------------------------------------------------------------------------------------------------------------")
                    sql = "select distinct 식별번호,부서이름,관리자.이름 from 부서,관리자 where 부서.부서_관리자ID = 관리자.식별_ID"
                    
                    cursor.execute(sql)
                    result=cursor.fetchall()
                    
                    for i in range(len(result)):
                        print(i,":",end = ' ')
                        
                        for j in range(len(result[i])):
                            print(result[i][j],end = ' | ')
                            
                        Dno = result[i][0]
                        sql = "select 부서위치.부서위치 from 부서,부서위치 where 부서.식별번호 = 부서위치.부서_식별번호 and 부서위치.부서_식별번호 = '{}'".format(Dno)
                        cursor.execute(sql)
                        location = cursor.fetchall()
                        for k in range(len(location)):
                            print(location[k][0],end = '  ')
                        print('|',end='  ')
                        sql = "select 관리영역.관리영역 from 부서,관리영역 where 부서.식별번호 = 관리영역.부서_식별번호 and 관리영역.부서_식별번호 = '{}'".format(Dno)
                        cursor.execute(sql)
                        area = cursor.fetchall()
                        for k in range(len(area)):
                            print(area[k][0],end = '  ')
                        print('\n')
                    
                elif num2 == '7':
                    print("정보 : 이름  /  성별  /  주민등록번호  /  식별ID  /  부서이름  /  도로명  /  건물번호  /  핸드폰번호 ")
                    print("----------------------------------------------------------------------------------------------------------------------------------")
                    sql = "select distinct 이름,성별,주민등록번호,식별_ID,부서이름,도로명,건물번호 from 부서,관리자 where 부서.식별번호 = 관리자.부서_식별번호"
                   
                    cursor.execute(sql)
                    result=cursor.fetchall()
                    
                    for i in range(len(result)):
                        print(i,":",end = ' ')
                        
                        for j in range(len(result[i])):
                            print(result[i][j],end = ' | ')

                        ssn = result[i][3]
                        sql = "select 핸드폰번호.핸드폰번호 from 관리자,핸드폰번호 where 관리자.식별_ID = 핸드폰번호.관리자_식별ID and 핸드폰번호.관리자_식별ID = '{}'".format(ssn)
                        cursor.execute(sql)
                        area = cursor.fetchall()
                        for k in range(len(area)):
                            print(area[k][0],end = '  ')
                        print('\n')

                    
                else:
                    print("잘못된 값 입니다.")

        else:
            print("존재하지 않는 관리자 식별 ID입니다. ")
            
       



    elif num1 == '3':
        while 1:
            Id = input("아이디: ")
            sql = "select EXISTS (select * from 사용자 where ID = '{}' ) as success".format(Id)
            if(check_exist(sql)):
                print("이미 존재하는 아이디 입니다.\n")
            else:
                Pw = input("비밀번호: ")
                name = input("이름: ")
                sex = '00'
                while 1:
                    sex = input("남자는 1, 여자는 2를 입력해주세요.\n성별: ")
                    if sex=='1':
                        sex = '남자'
                        break
                    elif sex == '2':
                        sex = '여자'
                        break
                    else:
                        print("잘못된 값입니다.")
                        
                phone_number = input("하이푼을 포함하여 11자리의 숫자를 입력하세요. EX:010-2222-3333\n핸드폰 번호: ")
            
                sql = "insert into 사용자(ID,Password,이름,성별,핸드폰번호) values ('{}','{}','{}','{}','{}')".format(Id,Pw,name,sex,phone_number)
                insert_into(sql)
                print("회원가입이 완료되었습니다.\n")
                break       


    elif num1 == '4':
                while 1:
                    Id = input("아이디: ")
                    Pw = input("비밀번호: ")
                    sql = "select EXISTS (select * from 사용자 where ID = '{}' and Password = '{}') as success".format(Id,Pw)
                    if(check_exist(sql)):
                        done = input("계정을 삭제합니까 ?(y or n): ")
                        if done == 'y':
                            sql = "delete from `사용자` where ID = '{}'".format(Id)
                            delete_from(sql)
                            print("이용 해주셔서 감사합니다.\n")
                            break
                        elif done == 'n':
                            break

                        else:
                            print("잘못된 값입니다.")

                    else:
                        print("존재하지 않는 아이디와 비밀번호 입니다.\n")
                        break       
        

    elif num1 == '5':
         while 1:
            print("등록을 취소하려면 exit을 입력하세요.\n")
            name = input("이름: ")
            if(name == 'exit'):
                  break
            ssn = input("식별번호: ")
            sql = "select EXISTS (select * from 관리자 where 식별_ID = '{}') as success".format(ssn)

         
            if(check_exist(sql)):
                print("이미 존재하는 사원입니다.\n")
            else:
                number_region = input("주민등록번호: ")
                sex = '00'
                while 1:
                    sex = input("남자는 1, 여자는 2를 입력해주세요.\n성별: ")
                    if sex=='1':
                        sex = '남자'
                        break
                    elif sex == '2':
                        sex = '여자'
                        break
                    else:
                        print("잘못된 값입니다.")
                        
                street = input("도로명을 입력하세요: ")
                building = input("건물번호를 입력하세요: ")
                
                phone_number = []
                num = 0
                                
                while 1:
                    temp = input("\n입력을 그만하려면 exit을 입력하세요. 최대 10개까지 입력 가능합니다.\n핸드폰 번호를 입력하세요: ")
                    if temp == 'exit':
                        
                        break
                    elif num == 9:
                        break
                    else:
                        phone_number.append(temp)
                        num = num+1
                        
                Dno = 'default'
                while 1:
                    Dno = input('부서번호를 입력하세요: ')
                    sql = "select EXISTS (select * from 부서 where 식별번호 = '{}') as success".format(Dno)
                    if(check_exist(sql)):
                        sql = "insert into 관리자(건물번호,도로명,식별_ID,주민등록번호,이름,성별,부서_식별번호) values ('{}','{}','{}','{}','{}','{}','{}')".format(building,street,ssn,number_region,name,sex,Dno)
                        insert_into(sql)
                        for i in range(len(phone_number)):
                            sql = "insert into 핸드폰번호 values ('{}','{}')".format(ssn,phone_number[i])
                            insert_into(sql)
                        
                        print("사원등록을 완료하였습니다\n")
                        break
                    else:
                        print("존재하지 않는 부서번호 입니다.\n")
                break
                

    else:
        print("잘못된 값 입니다.")

