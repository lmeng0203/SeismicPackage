!!------------------------ pickset_wrap.f90 --------------------------!!
!!------------------------ pickset_wrap.f90 --------------------------!!
!!------------------------ pickset_wrap.f90 --------------------------!!

!!!!!!!!!! DO NOT EDIT THIS FILE - it is machine generated !!!!!!!!!!

!!------------------------------ module -----------------------------------!!
!!------------------------------ module -----------------------------------!!
!!------------------------------ module -----------------------------------!!

      module pickset_wrap_module
      use pickset_module
      implicit none
      public

      type :: pickset_wrap_struct
        type(pickset_struct),pointer :: obj
      end type pickset_wrap_struct

      end module pickset_wrap_module

!!---------------------------- create ------------------------------------!!
!!---------------------------- create ------------------------------------!!
!!---------------------------- create ------------------------------------!!

      subroutine pickset_wrap_create (fpoint)  
      use pickset_wrap_module
      implicit none
      type(pickset_wrap_struct),intent(out)   :: fpoint
      type(pickset_struct)     ,pointer       :: obj           ! local

      nullify (obj)               ! needed for intel compiler.
      call pickset_create (obj)
      fpoint%obj => obj

      end subroutine pickset_wrap_create

!!---------------------------- delete ------------------------------------!!
!!---------------------------- delete ------------------------------------!!
!!---------------------------- delete ------------------------------------!!

      subroutine pickset_wrap_delete (fpoint)   
      use pickset_wrap_module
      implicit none
      type(pickset_wrap_struct),intent(inout) :: fpoint
      type(pickset_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call pickset_delete (obj)
      fpoint%obj => obj

      end subroutine pickset_wrap_delete

!!---------------------------- update ------------------------------------!!
!!---------------------------- update ------------------------------------!!
!!---------------------------- update ------------------------------------!!

      subroutine pickset_wrap_update (fpoint)     
      use pickset_wrap_module
      implicit none
      type(pickset_wrap_struct),intent(inout) :: fpoint
      type(pickset_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call pickset_update (obj)

      end subroutine pickset_wrap_update

!!---------------------------- wrapup ------------------------------------!!
!!---------------------------- wrapup ------------------------------------!!
!!---------------------------- wrapup ------------------------------------!!

      subroutine pickset_wrap_wrapup (fpoint)      
      use pickset_wrap_module
      implicit none
      type(pickset_wrap_struct),intent(inout) :: fpoint
      type(pickset_struct)     ,pointer       :: obj           ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call pickset_wrapup (obj)

      end subroutine pickset_wrap_wrapup

!!---------------------------- oneset ------------------------------------!!
!!---------------------------- oneset ------------------------------------!!
!!---------------------------- oneset ------------------------------------!!

      subroutine pickset_wrap_oneset (fpoint,ntr,  &
                                   hd,tr,lenhd,lentr,num)
      use pickset_wrap_module
      implicit none
      type(pickset_wrap_struct),intent(inout) :: fpoint
      integer               ,intent(in)    :: lenhd,lentr,num  ! argument
      integer               ,intent(inout) :: ntr              ! argument
      double precision      ,intent(inout) :: hd(lenhd,num)    ! argument
      real                  ,intent(inout) :: tr(lentr,num)    ! argument
      type(pickset_struct)     ,pointer       :: obj              ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
      call pickset (obj,ntr,hd,tr)                ! might be commented out.

      end subroutine pickset_wrap_oneset

!!---------------------------- twosets ------------------------------------!!
!!---------------------------- twosets ------------------------------------!!
!!---------------------------- twosets ------------------------------------!!

      subroutine pickset_wrap_twosets (fpoint,ntr,                 &
                                    hd1,tr1,lenhd1,lentr1,num1, &
                                    hd2,tr2,lenhd2,lentr2,num2)
      use pickset_wrap_module
      implicit none
      type(pickset_wrap_struct),intent(inout) :: fpoint
      integer               ,intent(in)    :: lenhd1,lentr1,num1 ! argument
      integer               ,intent(in)    :: lenhd2,lentr2,num2 ! argument
      integer               ,intent(inout) :: ntr                ! argument
      double precision      ,intent(inout) :: hd1(lenhd1,num1)   ! argument
      real                  ,intent(inout) :: tr1(lentr1,num1)   ! argument
      double precision      ,intent(inout) :: hd2(lenhd2,num2)   ! argument
      real                  ,intent(inout) :: tr2(lentr2,num2)   ! argument
      type(pickset_struct)     ,pointer       :: obj                ! local

      obj => fpoint%obj
      if (.not. associated(obj)) return
!!!!  call pickset (obj,ntr,hd1,tr1,hd2,tr2)      ! might be commented out.

      end subroutine pickset_wrap_twosets

!!----------------------------- end ---------------------------------------!!
!!----------------------------- end ---------------------------------------!!
!!----------------------------- end ---------------------------------------!!
