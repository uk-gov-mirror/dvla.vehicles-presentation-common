package uk.gov.gds.locality.api.model

import uk.gov.gds.locality.api.models.LocalAuthority

object LocalAuthorities {

  private lazy val localAuthorities = localAuthorityList.flatMap(la => Map[String, LocalAuthority](la._1 -> LocalAuthority(la._1, la._2, la._3))).toMap

  def localAuthorityForGssCode(gssCode: String) = localAuthorities(gssCode)

  private val localAuthorityList = List(
    ("E06000001", "00EB", "Hartlepool"),
    ("E06000002", "00EC", "Middlesbrough"),
    ("E06000003", "00EE", "Redcar and Cleveland"),
    ("E06000004", "00EF", "Stockton-on-Tees"),
    ("E06000005", "00EH", "Darlington"),
    ("E06000006", "00ET", "Halton"),
    ("E06000007", "00EU", "Warrington"),
    ("E06000008", "00EX", "Blackburn with Darwen"),
    ("E06000009", "00EY", "Blackpool"),
    ("E06000010", "00FA", "Kingston upon Hull, City of"),
    ("E06000011", "00FB", "East Riding of Yorkshire"),
    ("E06000012", "00FC", "North East Lincolnshire"),
    ("E06000013", "00FD", "North Lincolnshire"),
    ("E06000014", "00FF", "York"),
    ("E06000015", "00FK", "Derby"),
    ("E06000016", "00FN", "Leicester"),
    ("E06000017", "00FP", "Rutland"),
    ("E06000018", "00FY", "Nottingham"),
    ("E06000019", "00GA", "Herefordshire, County of"),
    ("E06000020", "00GF", "Telford and Wrekin"),
    ("E06000021", "00GL", "Stoke-on-Trent"),
    ("E06000022", "00HA", "Bath and North East Somerset"),
    ("E06000023", "00HB", "Bristol, City of"),
    ("E06000024", "00HC", "North Somerset"),
    ("E06000025", "00HD", "South Gloucestershire"),
    ("E06000026", "00HG", "Plymouth"),
    ("E06000027", "00HH", "Torbay"),
    ("E06000028", "00HN", "Bournemouth"),
    ("E06000029", "00HP", "Poole"),
    ("E06000030", "00HX", "Swindon"),
    ("E06000031", "00JA", "Peterborough"),
    ("E06000032", "00KA", "Luton"),
    ("E06000033", "00KF", "Southend-on-Sea"),
    ("E06000034", "00KG", "Thurrock"),
    ("E06000035", "00LC", "Medway"),
    ("E06000036", "00MA", "Bracknell Forest"),
    ("E06000037", "00MB", "West Berkshire"),
    ("E06000038", "00MC", "Reading"),
    ("E06000039", "00MD", "Slough"),
    ("E06000040", "00ME", "Windsor and Maidenhead"),
    ("E06000041", "00MF", "Wokingham"),
    ("E06000042", "00MG", "Milton Keynes"),
    ("E06000043", "00ML", "Brighton and Hove"),
    ("E06000044", "00MR", "Portsmouth"),
    ("E06000045", "00MS", "Southampton"),
    ("E06000046", "00MW", "Isle of Wight"),
    ("E06000047", "00EJ", "County Durham"),
    ("E06000048", "00EM", "Northumberland"),
    ("E06000049", "00EQ", "Cheshire East"),
    ("E06000050", "00EW", "Cheshire West and Chester"),
    ("E06000051", "00GG", "Shropshire"),
    ("E06000052", "00HE", "Cornwall"),
    ("E06000053", "00HF", "Isles of Scilly"),
    ("E06000054", "00HY", "Wiltshire"),
    ("E06000055", "00KB", "Bedford"),
    ("E06000056", "00KC", "Central Bedfordshire"),
    ("E07000004", "11UB", "Aylesbury Vale"),
    ("E07000005", "11UC", "Chiltern"),
    ("E07000006", "11UE", "South Bucks"),
    ("E07000007", "11UF", "Wycombe"),
    ("E07000008", "12UB", "Cambridge"),
    ("E07000009", "12UC", "East Cambridgeshire"),
    ("E07000010", "12UD", "Fenland"),
    ("E07000011", "12UE", "Huntingdonshire"),
    ("E07000012", "12UG", "South Cambridgeshire"),
    ("E07000026", "16UB", "Allerdale"),
    ("E07000027", "16UC", "Barrow-in-Furness"),
    ("E07000028", "16UD", "Carlisle"),
    ("E07000029", "16UE", "Copeland"),
    ("E07000030", "16UF", "Eden"),
    ("E07000031", "16UG", "South Lakeland"),
    ("E07000032", "17UB", "Amber Valley"),
    ("E07000033", "17UC", "Bolsover"),
    ("E07000034", "17UD", "Chesterfield"),
    ("E07000035", "17UF", "Derbyshire Dales"),
    ("E07000036", "17UG", "Erewash"),
    ("E07000037", "17UH", "High Peak"),
    ("E07000038", "17UJ", "North East Derbyshire"),
    ("E07000039", "17UK", "South Derbyshire"),
    ("E07000040", "18UB", "East Devon"),
    ("E07000041", "18UC", "Exeter"),
    ("E07000042", "18UD", "Mid Devon"),
    ("E07000043", "18UE", "North Devon"),
    ("E07000044", "18UG", "South Hams"),
    ("E07000045", "18UH", "Teignbridge"),
    ("E07000046", "18UK", "Torridge"),
    ("E07000047", "18UL", "West Devon"),
    ("E07000048", "19UC", "Christchurch"),
    ("E07000049", "19UD", "East Dorset"),
    ("E07000050", "19UE", "North Dorset"),
    ("E07000051", "19UG", "Purbeck"),
    ("E07000052", "19UH", "West Dorset"),
    ("E07000053", "19UJ", "Weymouth and Portland"),
    ("E07000061", "21UC", "Eastbourne"),
    ("E07000062", "21UD", "Hastings"),
    ("E07000063", "21UF", "Lewes"),
    ("E07000064", "21UG", "Rother"),
    ("E07000065", "21UH", "Wealden"),
    ("E07000066", "22UB", "Basildon"),
    ("E07000067", "22UC", "Braintree"),
    ("E07000068", "22UD", "Brentwood"),
    ("E07000069", "22UE", "Castle Point"),
    ("E07000070", "22UF", "Chelmsford"),
    ("E07000071", "22UG", "Colchester"),
    ("E07000072", "22UH", "Epping Forest"),
    ("E07000073", "22UJ", "Harlow"),
    ("E07000074", "22UK", "Maldon"),
    ("E07000075", "22UL", "Rochford"),
    ("E07000076", "22UN", "Tendring"),
    ("E07000077", "22UQ", "Uttlesford"),
    ("E07000078", "23UB", "Cheltenham"),
    ("E07000079", "23UC", "Cotswold"),
    ("E07000080", "23UD", "Forest of Dean"),
    ("E07000081", "23UE", "Gloucester"),
    ("E07000082", "23UF", "Stroud"),
    ("E07000083", "23UG", "Tewkesbury"),
    ("E07000084", "24UB", "Basingstoke and Deane"),
    ("E07000085", "24UC", "East Hampshire"),
    ("E07000086", "24UD", "Eastleigh"),
    ("E07000087", "24UE", "Fareham"),
    ("E07000088", "24UF", "Gosport"),
    ("E07000089", "24UG", "Hart"),
    ("E07000090", "24UH", "Havant"),
    ("E07000091", "24UJ", "New Forest"),
    ("E07000092", "24UL", "Rushmoor"),
    ("E07000093", "24UN", "Test Valley"),
    ("E07000094", "24UP", "Winchester"),
    ("E07000095", "26UB", "Broxbourne"),
    ("E07000096", "26UC", "Dacorum"),
    ("E07000097", "26UD", "East Hertfordshire"),
    ("E07000098", "26UE", "Hertsmere"),
    ("E07000099", "26UF", "North Hertfordshire"),
    ("E07000101", "26UH", "Stevenage"),
    ("E07000102", "26UJ", "Three Rivers"),
    ("E07000103", "26UK", "Watford"),
    ("E07000105", "29UB", "Ashford"),
    ("E07000106", "29UC", "Canterbury"),
    ("E07000107", "29UD", "Dartford"),
    ("E07000108", "29UE", "Dover"),
    ("E07000109", "29UG", "Gravesham"),
    ("E07000110", "29UH", "Maidstone"),
    ("E07000111", "29UK", "Sevenoaks"),
    ("E07000112", "29UL", "Shepway"),
    ("E07000113", "29UM", "Swale"),
    ("E07000114", "29UN", "Thanet"),
    ("E07000115", "29UP", "Tonbridge and Malling"),
    ("E07000116", "29UQ", "Tunbridge Wells"),
    ("E07000117", "30UD", "Burnley"),
    ("E07000118", "30UE", "Chorley"),
    ("E07000119", "30UF", "Fylde"),
    ("E07000120", "30UG", "Hyndburn"),
    ("E07000121", "30UH", "Lancaster"),
    ("E07000122", "30UJ", "Pendle"),
    ("E07000123", "30UK", "Preston"),
    ("E07000124", "30UL", "Ribble Valley"),
    ("E07000125", "30UM", "Rossendale"),
    ("E07000126", "30UN", "South Ribble"),
    ("E07000127", "30UP", "West Lancashire"),
    ("E07000128", "30UQ", "Wyre"),
    ("E07000129", "31UB", "Blaby"),
    ("E07000130", "31UC", "Charnwood"),
    ("E07000131", "31UD", "Harborough"),
    ("E07000132", "31UE", "Hinckley and Bosworth"),
    ("E07000133", "31UG", "Melton"),
    ("E07000134", "31UH", "North West Leicestershire"),
    ("E07000135", "31UJ", "Oadby and Wigston"),
    ("E07000136", "32UB", "Boston"),
    ("E07000137", "32UC", "East Lindsey"),
    ("E07000138", "32UD", "Lincoln"),
    ("E07000139", "32UE", "North Kesteven"),
    ("E07000140", "32UF", "South Holland"),
    ("E07000141", "32UG", "South Kesteven"),
    ("E07000142", "32UH", "West Lindsey"),
    ("E07000143", "33UB", "Breckland"),
    ("E07000144", "33UC", "Broadland"),
    ("E07000145", "33UD", "Great Yarmouth"),
    ("E07000146", "33UE", "King's Lynn and West Norfolk"),
    ("E07000147", "33UF", "North Norfolk"),
    ("E07000148", "33UG", "Norwich"),
    ("E07000149", "33UH", "South Norfolk"),
    ("E07000150", "34UB", "Corby"),
    ("E07000151", "34UC", "Daventry"),
    ("E07000152", "34UD", "East Northamptonshire"),
    ("E07000153", "34UE", "Kettering"),
    ("E07000154", "34UF", "Northampton"),
    ("E07000155", "34UG", "South Northamptonshire"),
    ("E07000156", "34UH", "Wellingborough"),
    ("E07000163", "36UB", "Craven"),
    ("E07000164", "36UC", "Hambleton"),
    ("E07000165", "36UD", "Harrogate"),
    ("E07000166", "36UE", "Richmondshire"),
    ("E07000167", "36UF", "Ryedale"),
    ("E07000168", "36UG", "Scarborough"),
    ("E07000169", "36UH", "Selby"),
    ("E07000170", "37UB", "Ashfield"),
    ("E07000171", "37UC", "Bassetlaw"),
    ("E07000172", "37UD", "Broxtowe"),
    ("E07000173", "37UE", "Gedling"),
    ("E07000174", "37UF", "Mansfield"),
    ("E07000175", "37UG", "Newark and Sherwood"),
    ("E07000176", "37UJ", "Rushcliffe"),
    ("E07000177", "38UB", "Cherwell"),
    ("E07000178", "38UC", "Oxford"),
    ("E07000179", "38UD", "South Oxfordshire"),
    ("E07000180", "38UE", "Vale of White Horse"),
    ("E07000181", "38UF", "West Oxfordshire"),
    ("E07000187", "40UB", "Mendip"),
    ("E07000188", "40UC", "Sedgemoor"),
    ("E07000189", "40UD", "South Somerset"),
    ("E07000190", "40UE", "Taunton Deane"),
    ("E07000191", "40UF", "West Somerset"),
    ("E07000192", "41UB", "Cannock Chase"),
    ("E07000193", "41UC", "East Staffordshire"),
    ("E07000194", "41UD", "Lichfield"),
    ("E07000195", "41UE", "Newcastle-under-Lyme"),
    ("E07000196", "41UF", "South Staffordshire"),
    ("E07000197", "41UG", "Stafford"),
    ("E07000198", "41UH", "Staffordshire Moorlands"),
    ("E07000199", "41UK", "Tamworth"),
    ("E07000200", "42UB", "Babergh"),
    ("E07000201", "42UC", "Forest Heath"),
    ("E07000202", "42UD", "Ipswich"),
    ("E07000203", "42UE", "Mid Suffolk"),
    ("E07000204", "42UF", "St Edmundsbury"),
    ("E07000205", "42UG", "Suffolk Coastal"),
    ("E07000206", "42UH", "Waveney"),
    ("E07000207", "43UB", "Elmbridge"),
    ("E07000208", "43UC", "Epsom and Ewell"),
    ("E07000209", "43UD", "Guildford"),
    ("E07000210", "43UE", "Mole Valley"),
    ("E07000211", "43UF", "Reigate and Banstead"),
    ("E07000212", "43UG", "Runnymede"),
    ("E07000213", "43UH", "Spelthorne"),
    ("E07000214", "43UJ", "Surrey Heath"),
    ("E07000215", "43UK", "Tandridge"),
    ("E07000216", "43UL", "Waverley"),
    ("E07000217", "43UM", "Woking"),
    ("E07000218", "44UB", "North Warwickshire"),
    ("E07000219", "44UC", "Nuneaton and Bedworth"),
    ("E07000220", "44UD", "Rugby"),
    ("E07000221", "44UE", "Stratford-on-Avon"),
    ("E07000222", "44UF", "Warwick"),
    ("E07000223", "45UB", "Adur"),
    ("E07000224", "45UC", "Arun"),
    ("E07000225", "45UD", "Chichester"),
    ("E07000226", "45UE", "Crawley"),
    ("E07000227", "45UF", "Horsham"),
    ("E07000228", "45UG", "Mid Sussex"),
    ("E07000229", "45UH", "Worthing"),
    ("E07000234", "47UB", "Bromsgrove"),
    ("E07000235", "47UC", "Malvern Hills"),
    ("E07000236", "47UD", "Redditch"),
    ("E07000237", "47UE", "Worcester"),
    ("E07000238", "47UF", "Wychavon"),
    ("E07000239", "47UG", "Wyre Forest"),
    ("E07000240", "26UG", "St Albans"),
    ("E07000241", "26UL", "Welwyn Hatfield"),
    ("E08000001", "00BL", "Bolton"),
    ("E08000002", "00BM", "Bury"),
    ("E08000003", "00BN", "Manchester"),
    ("E08000004", "00BP", "Oldham"),
    ("E08000005", "00BQ", "Rochdale"),
    ("E08000006", "00BR", "Salford"),
    ("E08000007", "00BS", "Stockport"),
    ("E08000008", "00BT", "Tameside"),
    ("E08000009", "00BU", "Trafford"),
    ("E08000010", "00BW", "Wigan"),
    ("E08000011", "00BX", "Knowsley"),
    ("E08000012", "00BY", "Liverpool"),
    ("E08000013", "00BZ", "St. Helens"),
    ("E08000014", "00CA", "Sefton"),
    ("E08000015", "00CB", "Wirral"),
    ("E08000016", "00CC", "Barnsley"),
    ("E08000017", "00CE", "Doncaster"),
    ("E08000018", "00CF", "Rotherham"),
    ("E08000019", "00CG", "Sheffield"),
    ("E08000020", "00CH", "Gateshead"),
    ("E08000021", "00CJ", "Newcastle upon Tyne"),
    ("E08000022", "00CK", "North Tyneside"),
    ("E08000023", "00CL", "South Tyneside"),
    ("E08000024", "00CM", "Sunderland"),
    ("E08000025", "00CN", "Birmingham"),
    ("E08000026", "00CQ", "Coventry"),
    ("E08000027", "00CR", "Dudley"),
    ("E08000028", "00CS", "Sandwell"),
    ("E08000029", "00CT", "Solihull"),
    ("E08000030", "00CU", "Walsall"),
    ("E08000031", "00CW", "Wolverhampton"),
    ("E08000032", "00CX", "Bradford"),
    ("E08000033", "00CY", "Calderdale"),
    ("E08000034", "00CZ", "Kirklees"),
    ("E08000035", "00DA", "Leeds"),
    ("E08000036", "00DB", "Wakefield"),
    ("E09000001", "00AA", "City of London"),
    ("E09000002", "00AB", "Barking and Dagenham"),
    ("E09000003", "00AC", "Barnet"),
    ("E09000004", "00AD", "Bexley"),
    ("E09000005", "00AE", "Brent"),
    ("E09000006", "00AF", "Bromley"),
    ("E09000007", "00AG", "Camden"),
    ("E09000008", "00AH", "Croydon"),
    ("E09000009", "00AJ", "Ealing"),
    ("E09000010", "00AK", "Enfield"),
    ("E09000011", "00AL", "Greenwich"),
    ("E09000012", "00AM", "Hackney"),
    ("E09000013", "00AN", "Hammersmith and Fulham"),
    ("E09000014", "00AP", "Haringey"),
    ("E09000015", "00AQ", "Harrow"),
    ("E09000016", "00AR", "Havering"),
    ("E09000017", "00AS", "Hillingdon"),
    ("E09000018", "00AT", "Hounslow"),
    ("E09000019", "00AU", "Islington"),
    ("E09000020", "00AW", "Kensington and Chelsea"),
    ("E09000021", "00AX", "Kingston upon Thames"),
    ("E09000022", "00AY", "Lambeth"),
    ("E09000023", "00AZ", "Lewisham"),
    ("E09000024", "00BA", "Merton"),
    ("E09000025", "00BB", "Newham"),
    ("E09000026", "00BC", "Redbridge"),
    ("E09000027", "00BD", "Richmond upon Thames"),
    ("E09000028", "00BE", "Southwark"),
    ("E09000029", "00BF", "Sutton"),
    ("E09000030", "00BG", "Tower Hamlets"),
    ("E09000031", "00BH", "Waltham Forest"),
    ("E09000032", "00BJ", "Wandsworth"),
    ("E09000033", "00BK", "Westminster"),
    ("S12000005", "00QF", "Clackmannanshire"),
    ("S12000006", "00QH", "Dumfries and Galloway"),
    ("S12000008", "00QK", "East Ayrshire"),
    ("S12000010", "00QM", "East Lothian"),
    ("S12000011", "00QN", "East Renfrewshire"),
    ("S12000013", "00RJ", "Eilean Siar"),
    ("S12000014", "00QQ", "Falkirk"),
    ("S12000015", "00QR", "Fife"),
    ("S12000017", "00QT", "Highland"),
    ("S12000018", "00QU", "Inverclyde"),
    ("S12000019", "00QW", "Midlothian"),
    ("S12000020", "00QX", "Moray"),
    ("S12000021", "00QY", "North Ayrshire"),
    ("S12000023", "00RA", "Orkney Islands"),
    ("S12000024", "00RB", "Perth and Kinross"),
    ("S12000026", "00QE", "Scottish Borders"),
    ("S12000027", "00RD", "Shetland Islands"),
    ("S12000028", "00RE", "South Ayrshire"),
    ("S12000029", "00RF", "South Lanarkshire"),
    ("S12000030", "00RG", "Stirling"),
    ("S12000033", "00QA", "Aberdeen City"),
    ("S12000034", "00QB", "Aberdeenshire"),
    ("S12000035", "00QD", "Argyll and Bute"),
    ("S12000036", "00QP", "City of Edinburgh"),
    ("S12000038", "00RC", "Renfrewshire"),
    ("S12000039", "00QG", "West Dunbartonshire"),
    ("S12000040", "00RH", "West Lothian"),
    ("S12000041", "00QC", "Angus"),
    ("S12000042", "00QJ", "Dundee City"),
    ("S12000044", "00QZ", "North Lanarkshire"),
    ("S12000045", "00QL", "East Dunbartonshire"),
    ("S12000046", "00QS", "Glasgow City"),
    ("W06000001", "00NA", "Isle of Anglesey"),
    ("W06000002", "00NC", "Gwynedd"),
    ("W06000003", "00NE", "Conwy"),
    ("W06000004", "00NG", "Denbighshire"),
    ("W06000005", "00NJ", "Flintshire"),
    ("W06000006", "00NL", "Wrexham"),
    ("W06000008", "00NQ", "Ceredigion"),
    ("W06000009", "00NS", "Pembrokeshire"),
    ("W06000010", "00NU", "Carmarthenshire"),
    ("W06000011", "00NX", "Swansea"),
    ("W06000012", "00NZ", "Neath Port Talbot"),
    ("W06000013", "00PB", "Bridgend"),
    ("W06000014", "00PD", "The Vale of Glamorgan"),
    ("W06000015", "00PT", "Cardiff"),
    ("W06000016", "00PF", "Rhondda Cynon Taf"),
    ("W06000018", "00PK", "Caerphilly"),
    ("W06000019", "00PL", "Blaenau Gwent"),
    ("W06000020", "00PM", "Torfaen"),
    ("W06000021", "00PP", "Monmouthshire"),
    ("W06000022", "00PR", "Newport"),
    ("W06000023", "00NN", "Powys"),
    ("W06000024", "00PH", "Merthyr Tydfil")
  )
}