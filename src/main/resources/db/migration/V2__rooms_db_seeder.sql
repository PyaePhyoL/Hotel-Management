INSERT INTO room_types (id, description, price, capacity)
VALUES
    ('SR1', 'Single room', 30000, 1),
    ('SR2', 'Single room share bathroom', 25000, 1),
    ('DR1', 'Double room', 40000, 2),
    ('DR2', 'Double room share bathroom', 35000, 2),
    ('FR1', 'Family room', 48000, 4)
;


INSERT INTO rooms (room_no, floor, current_status, room_type_id, notes)
VALUES
    (1, 'FOURTH','AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (2, 'FOURTH','AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (3, 'FOURTH','AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (4, 'FOURTH','AVAILABLE', 'SR1', 'ရေချိုးခန်းတွဲလျက်'),
    (10, 'FOURTH', 'AVAILABLE', 'FR1', 'အခန်းကျယ်၊ ၀ရံတာ၊ ပြတင်း‌ပေါက်ပါ'),
    (11, 'FOURTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (12, 'FOURTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (13, 'FOURTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (14, 'FOURTH',  'AVAILABLE', 'SR1', 'ရေချိုးခန်းတွဲလျက်'),


    (5, 'FIFTH', 'AVAILABLE', 'FR1', 'ရေချိုးခန်းတွဲလျက်'),
    (6, 'FIFTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (7, 'FIFTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (8, 'FIFTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (9, 'FIFTH',  'AVAILABLE', 'SR1', 'ရေချိုးခန်းတွဲလျက်'),
    (15, 'FIFTH', 'AVAILABLE', 'FR1', 'ရေချိုးခန်းတွဲလျက်'),
    (16, 'FIFTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (17, 'FIFTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (19, 'FIFTH',  'AVAILABLE', 'SR1', 'ရေချိုးခန်းတွဲလျက်'),


    (701, 'SEVENTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (702, 'SEVENTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (703, 'SEVENTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (704, 'SEVENTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (705, 'SEVENTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (706, 'SEVENTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (707, 'SEVENTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (715, 'SEVENTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (716, 'SEVENTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (717, 'SEVENTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (718, 'SEVENTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (719, 'SEVENTH', 'AVAILABLE', 'SR1', 'ရေချိုးခန်းတွဲလျက်'),
    (720, 'SEVENTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (721, 'SEVENTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (722, 'SEVENTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (723, 'SEVENTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
  --  (724, 'SEVENTH', 0, 0, 0, 'STORE'),


    (801, 'EIGHTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (802, 'EIGHTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (803, 'EIGHTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (804, 'EIGHTH', 'AVAILABLE', 'DR1', 'ရေချိုးခန်းတွဲလျက်'),
    (806, 'EIGHTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (809, 'EIGHTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (810, 'EIGHTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (811, 'EIGHTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (812, 'EIGHTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (813, 'EIGHTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (814, 'EIGHTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (815, 'EIGHTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (816, 'EIGHTH', 'AVAILABLE', 'DR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့'),
    (817, 'EIGHTH', 'AVAILABLE', 'SR2', 'ရေချိုးခန်းကိုယ်ပိုင်၊ ကိုယ်ပိုင်သော့')

;

INSERT INTO room_pricing_rules (room_type_id, stay_type, no_of_guests, hours, price)
VALUES
    ('SR1', 'NORMAL', 1, null, 30000),
    ('SR1', 'NORMAL', 2, null, 40000),
    ('SR2', 'NORMAL', 1, null, 25000),
    ('SR2', 'NORMAL', 1, null, 35000),
    ('DR1', 'NORMAL', 1, null, 35000),
    ('DR1', 'NORMAL', 2, null, 40000),
    ('DR1', 'NORMAL', 3, null, 55000),
    ('DR2', 'NORMAL', 1, null, 30000),
    ('DR2', 'NORMAL', 2, null, 35000),
    ('DR2', 'NORMAL', 3, null, 50000),
    ('FR1', 'NORMAL', 2, null, 48000),
    ('FR1', 'NORMAL', 3, null, 60000),
    ('FR1', 'NORMAL', 4, null, 70000),

    ('SR1', 'LONG', 1, null, 30000),
    ('SR1', 'LONG', 2, null, 40000),
    ('SR2', 'LONG', 1, null, 25000),
    ('SR2', 'LONG', 1, null, 35000),
    ('DR1', 'LONG', 1, null, 35000),
    ('DR1', 'LONG', 2, null, 40000),
    ('DR1', 'LONG', 3, null, 55000),
    ('DR2', 'LONG', 1, null, 30000),
    ('DR2', 'LONG', 2, null, 35000),
    ('DR2', 'LONG', 3, null, 50000),
    ('FR1', 'LONG', 2, null, 48000),
    ('FR1', 'LONG', 3, null, 60000),
    ('FR1', 'LONG', 4, null, 70000),

    ('DR1', 'SECTION', null, 3, 30000),
    ('DR2', 'SECTION', null, 3, 25000)
;
